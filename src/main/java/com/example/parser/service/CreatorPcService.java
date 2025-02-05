package com.example.parser.service;

import com.example.parser.model.Pc;
import com.example.parser.model.PcMarker;
import com.example.parser.model.hotline.CpuHotLine;
import com.example.parser.model.hotline.GpuHotLine;
import com.example.parser.model.hotline.MemoryHotLine;
import com.example.parser.model.hotline.MotherBoardHotLine;
import com.example.parser.model.hotline.PowerSupplierHotLine;
import com.example.parser.model.hotline.SsdHotLine;
import com.example.parser.repository.CpuHotLineRepository;
import com.example.parser.repository.GpuHotLineRepository;
import com.example.parser.repository.MemoryHotLineRepository;
import com.example.parser.repository.MotherBoardHotLineRepository;
import com.example.parser.repository.PcHotLineRepository;
import com.example.parser.repository.PowerSupplierHotLineRepository;
import com.example.parser.repository.SsdHotLineRepository;
import com.example.parser.service.hotline.HotlineDataUpdateCoordinatorService;
import com.example.parser.service.userbenchmark.CpuUserBenchmarkService;
import com.example.parser.service.userbenchmark.GpuUserBenchmarkService;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class CreatorPcService {
    private final static int MIN_PROPOSITION_QUANTITY_DEFAULT = 5;
    private final static double CASE_PRICE_MIN = 15.0;
    private final static double CASE_PRICE_AVG = 25.0;
    private final static double CASE_PRICE_MAX = 50.0;
    private final CpuHotLineRepository cpuHotLineRepository;
    private final MotherBoardHotLineRepository motherBoardHotLineRepository;
    private final MemoryHotLineRepository memoryHotLineRepository;
    private final GpuHotLineRepository gpuHotLineRepository;
    private final SsdHotLineRepository ssdHotLineRepository;
    private final PowerSupplierHotLineRepository powerSupplierHotLineRepository;
    private final PcHotLineRepository pcHotLineRepository;
    private final ExcelExporter excelExporter;
    private final CpuUserBenchmarkService cpuUserBenchmarkService;
    private final GpuUserBenchmarkService gpuUserBenchmarkService;
    private final HotlineDataUpdateCoordinatorService hotlineDataUpdateCoordinatorService;

    public List<Pc> getAll() {
        return pcHotLineRepository.findPcListWithNonZeroPriceForFpsOrdered();
    }

    public List<Pc> getAllByBestPrice() {
        final List<Pc> allByMarkerOrderByPredictionPrice = pcHotLineRepository
                .findAllByMarkerOrderByPredictionPrice(PcMarker.BEST_PRICE);
        return allByMarkerOrderByPredictionPrice;
    }

    public boolean updateDataAndCreatePcList(
            boolean updateUserBenchmarkCpu,
            boolean updateUserBenchmarkGpu,
            boolean updateHotline,
            boolean createPcList,
            boolean saveReportToExel) {
        try {
            log.info("Starting data update process ");

            if (updateUserBenchmarkCpu) {
                cpuUserBenchmarkService.loadAndSaveNewItems();
                cpuUserBenchmarkService.updateMissingSpecifications();
            }

            if (updateUserBenchmarkGpu) {
                gpuUserBenchmarkService.loadAndSaveNewItems();
            }

            if (updateHotline) {
                hotlineDataUpdateCoordinatorService.updateAllData();
            }

            if (createPcList) {
                createFilterAndSaveOptimalPcList();
            }

            if (saveReportToExel) {
                exportToExcelPcList("pc_configuration",
                        pcHotLineRepository.findPcListWithNonZeroPriceForFpsOrdered());
            }

            log.info("Data update process completed successfully");

        } catch (Exception e) {
            log.error("An error occurred during the update process: ", e);
            return false;
        }

        return true;
    }

    public void exportToExcelPcList(String fileName, List<Pc> pcList) {
        log.info("Start save file to Excel");

        long executionTime = measureExecutionTime(() -> {

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm");
            String formattedDate = now.format(formatter);

            if (!pcList.isEmpty()) {
                String documentsPath = System.getProperty("user.home") + File.separator + "Documents";
                String fulFileName = fileName + " " + formattedDate + ".xlsx";
                String fullPath = Paths.get(documentsPath, fulFileName).toString();

                excelExporter.exportToExcelPcConfiguration(pcList, fullPath);
                log.info("Экспорт списка ПК в Excel завершён. Файл сохранён по пути: {}", fullPath);
            } else {
                log.warn("Список ПК пуст, экспорт не выполнен.");
            }

        });

        log.info("Время выполнения экспорта в Excel: {} мс", executionTime);
    }

    private void createFilterAndSaveOptimalPcList() {
        log.info("Start creating and filtering PC list");
        try {
            pcHotLineRepository.deleteAll();
            List<Pc> allPcList = pcHotLineRepository.saveAll(createPc());
            List<Pc> optimalPcList = removeItemsWithUncompetitivePrice(allPcList);
            insertMarker(optimalPcList, PcMarker.BEST_PRICE);
            pcHotLineRepository.saveAll(optimalPcList);
        } catch (Exception e) {
            log.error("Error occurred during the process: ", e);
        }
        log.info("Finished creating and saving optimal PC list");
    }


    private void insertMarker(List<Pc> pcList, PcMarker marker) {
        pcList.stream()
                .peek(pc -> pc.setMarker(marker))
                .collect(Collectors.toList());
    }

    private List<Pc> createPc() {
        pcHotLineRepository.deleteAll();
        List<CpuHotLine> cpus = cpuHotLineRepository.findCpusWithMinPropositions(MIN_PROPOSITION_QUANTITY_DEFAULT);
        List<MotherBoardHotLine> motherBoards = motherBoardHotLineRepository.findMinPriceGroupedByChipsetWithConditions(MIN_PROPOSITION_QUANTITY_DEFAULT);
        List<MemoryHotLine> memories = memoryHotLineRepository.findMinPriceGroupedByTypeWithConditions(MIN_PROPOSITION_QUANTITY_DEFAULT);
        List<GpuHotLine> gpus = gpuHotLineRepository.findGroupedGpusByMinAvgPrice(MIN_PROPOSITION_QUANTITY_DEFAULT);
        SsdHotLine ssdFromDb = ssdHotLineRepository.findTopByAvgPrice(MIN_PROPOSITION_QUANTITY_DEFAULT);
        List<PowerSupplierHotLine> powerSupplierHotLines = powerSupplierHotLineRepository.findGroupedByPowerWithMinAvgPrice(MIN_PROPOSITION_QUANTITY_DEFAULT);

        validateData(cpus, motherBoards, memories, gpus, ssdFromDb, powerSupplierHotLines);

        List<Pc> pcs = new ArrayList<>();

        cpus.forEach(cpu -> {
            Optional<MotherBoardHotLine> motherboardOpt = getMotherboardFromCpuBySocket(motherBoards, cpu);
            if (motherboardOpt.isPresent()) {
                MotherBoardHotLine motherboard = motherboardOpt.get();
                MemoryHotLine memory = getMemoryFromMotherBoardBySocketType(memories, motherboard);
                pcs.addAll(video(gpus, cpu, motherboard, memory, ssdFromDb, powerSupplierHotLines));
            }
        });
        log.info("Computer configurations were successfully assembled");
        return filterPc(pcs);
    }


    private List<Pc> removeItemsWithUncompetitivePrice(List<Pc> pcList) {
        boolean process = true;

        while (process) {
            process = false;

            for (int i = 0; i < pcList.size() - 1; i++) {
                if (pcList.get(i).getPrice().compareTo(pcList.get(i + 1).getPrice()) > 0) {
                    pcList.get(i).setPrice(BigDecimal.ZERO);
                    process = true;
                }
            }
            pcList.removeIf(pc -> pc.getPrice().compareTo(BigDecimal.ZERO) == 0);
        }
        return pcList;
    }

    private List<Pc> filterPc(List<Pc> pcList) {
        return pcList.stream()
                .sorted(
                        Comparator
                                .comparing(Pc::getPredictionGpuFpsFHD, Comparator.nullsLast(Comparator.naturalOrder()))
                                .thenComparing(Pc::getPrice, Comparator.nullsLast(Comparator.naturalOrder()))
                )
                .collect(Collectors.toList());
    }


    private void validateData(List<CpuHotLine> cpus, List<MotherBoardHotLine> motherBoards, List<MemoryHotLine> memories, List<GpuHotLine> gpus, SsdHotLine ssdFromDb, List<PowerSupplierHotLine> powerSupplierHotLines) {
        if (cpus.isEmpty() || motherBoards.isEmpty() || memories.isEmpty() || gpus.isEmpty() || ssdFromDb == null || powerSupplierHotLines.isEmpty()) {
            throw new IllegalArgumentException("One or more required data lists are empty.");
        }
    }

    private List<Pc> video(List<GpuHotLine> gpus, CpuHotLine cpu, MotherBoardHotLine mb, MemoryHotLine memory, SsdHotLine ssd, List<PowerSupplierHotLine> powerSupplierHotLines) {
        List<Pc> pcs = new ArrayList<>();

        gpus.forEach(gpu -> {
            Pc pc = new Pc();
            initializePc(pc, gpu, cpu, mb, memory, ssd, powerSupplierHotLines);
            pcs.add(pc);
        });

        return pcs;
    }

    private void initializePc(Pc pc, GpuHotLine gpu, CpuHotLine cpu, MotherBoardHotLine mb, MemoryHotLine memory, SsdHotLine ssd, List<PowerSupplierHotLine> powerSupplierHotLines) {
        pc.setGpu(gpu);
        pc.setCpu(cpu);
        pc.setMotherboard(mb);
        pc.setMemory(memory);
        pc.setSsd(ssd);
        pc.setPowerSupplier(getPowerSupply(gpu, powerSupplierHotLines));
        pc.setPrice(BigDecimal.valueOf(calculatePrice(pc)));
        pc.setAvgGpuBench(gpu.getUserBenchmarkGpu().getAvgBench());
        pc.setDesktopScore(cpu.getUserBenchmarkCpu().getDesktopScore());
        pc.setGamingScore(cpu.getUserBenchmarkCpu().getGamingScore());
        pc.setWorkstationScore(cpu.getUserBenchmarkCpu().getWorkstationScore());
        pc.setPredictionGpuFpsFHD(
                calculationPredictionGpuFpsHd(
                        cpu.getUserBenchmarkCpu().getGamingScore(),
                        gpu.getUserBenchmarkGpu().getAvgBench()
                )
        );
        pc.setPriceForFps(calculatePriceForFpc(pc));
    }

    private double calculatePrice(Pc pc) {
        Double avgPriceCpu = pc.getCpu().getAvgPrice();
        double coolingPrice = calculateCoolingPrice(pc.getCpu());
        avgPriceCpu += calculateCasePrice(pc.getCpu());

        Double avgPriceMb = getSafeAvgPrice(pc.getMotherboard().getAvgPrice());
        Double avgPriceMemory = getSafeAvgPrice(pc.getMemory().getAvgPrice());
        Double avgPriceGpu = getSafeAvgPrice(pc.getGpu().getAvgPrice());
        Double avgPriceSsd = getSafeAvgPrice(pc.getSsd().getAvgPrice());
        Double avgPricePs = getSafeAvgPrice(pc.getPowerSupplier().getAvgPrice());

        return avgPriceCpu + coolingPrice + avgPriceMb + avgPriceMemory + avgPriceGpu + avgPriceSsd + avgPricePs;
    }

    private double calculateCoolingPrice(CpuHotLine cpu) {
        if (cpu.getPackageType() != null && cpu.getPackageType().contains("Tray")) {
            if (cpu.getName().contains("i9") || cpu.getName().contains("Ryzen 9")) {
                return 70;
            } else if (cpu.getName().contains("i7") || cpu.getName().contains("Ryzen 7")) {
                return 40;
            } else if (cpu.getName().contains("i5") || cpu.getName().contains("Ryzen 5")) {
                return 20;
            }
        }
        if (cpu.getPackageType() != null && cpu.getPackageType().contains("Box")) {
            if (cpu.getName().contains("Ryzen 9") && cpu.getName().contains("X")) {
                return 70;
            } else if (cpu.getName().contains("Ryzen 7 ") && cpu.getName().contains("X")) {
                return 40;
            } else if (cpu.getName().contains("Ryzen 5 ") && cpu.getName().contains("X")) {
                return 30;
            } else if (cpu.getName().contains("i9") && cpu.getName().contains("K")) {
                return 70;
            } else if (cpu.getName().contains("i7") && cpu.getName().contains("K")) {
                return 40;
            } else if (cpu.getName().contains("i5") && cpu.getName().contains("K")) {
                return 30;
            }
        }
        return 0;
    }

    private double calculateCasePrice(CpuHotLine cpu) {
        if (cpu.getPackageType() != null && cpu.getPackageType().contains("Tray")) {
            if (cpu.getName().contains("i9") || cpu.getName().contains("Ryzen 9")) {
                return CASE_PRICE_MAX;
            } else if (cpu.getName().contains("i7") || cpu.getName().contains("Ryzen 7")) {
                return CASE_PRICE_AVG;
            }
        }
        if (cpu.getPackageType() != null && cpu.getPackageType().contains("Box")) {
            if ((cpu.getName().contains("Ryzen 9") && cpu.getName().contains("X")) ||
                    (cpu.getName().contains("i9") && cpu.getName().contains("K"))) {
                return CASE_PRICE_MAX;
            } else if ((cpu.getName().contains("Ryzen 7") && cpu.getName().contains("X")) ||
                    (cpu.getName().contains("i7") && cpu.getName().contains("K"))) {
                return CASE_PRICE_AVG;
            } else if ((cpu.getName().contains("Ryzen 5") && cpu.getName().contains("X")) ||
                    (cpu.getName().contains("i5") && cpu.getName().contains("K"))) {
                return CASE_PRICE_AVG;
            }
        }
        return CASE_PRICE_MIN;
    }

    private double getSafeAvgPrice(Double price) {
        return price != null ? price : 0;
    }

    private int calculationPredictionGpuFpsHd(double cpuGamingScore, double avgGpuBench) {
        return (int) (cpuGamingScore * avgGpuBench / 100);
    }

    private PowerSupplierHotLine getPowerSupply(GpuHotLine gpu, List<PowerSupplierHotLine> powerSupplierHotLines) {
        return powerSupplierHotLines.stream()
                .sorted(Comparator.comparingInt(PowerSupplierHotLine::getPower))
                .filter(p -> p.getPower() >= gpu.getUserBenchmarkGpu().getPowerRequirement())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No suitable power supply found for GPU: " + gpu.getName()));
    }

    private MemoryHotLine getMemoryFromMotherBoardBySocketType(List<MemoryHotLine> memoryHotLineList, MotherBoardHotLine motherBoardHotLine) {
        final String memoryType = motherBoardHotLine.getMemoryType();

        if (memoryType == null) {
            throw new IllegalArgumentException("Motherboard memory type is null.");
        }

        return memoryHotLineList.stream()
                .filter(m -> m.getType().contains(memoryType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No memory found for type: " + memoryType));
    }

    private Optional<MotherBoardHotLine> getMotherboardFromCpuBySocket(List<MotherBoardHotLine> motherBoards, CpuHotLine cpu) {
        int propositionQuantityThreshold = MIN_PROPOSITION_QUANTITY_DEFAULT;

        while (propositionQuantityThreshold >= 2) {
            List<MotherBoardHotLine> mbList = filterMotherboardsByCpu(motherBoards, cpu);

            if (!mbList.isEmpty()) {
                return mbList.stream()
                        .sorted(Comparator.comparing(MotherBoardHotLine::getAvgPrice, Comparator.nullsLast(Comparator.naturalOrder())))
                        .findFirst();
            }

            propositionQuantityThreshold--;
            motherBoards = updateMotherBoardsList(propositionQuantityThreshold);
        }

        return Optional.empty();
    }

    private List<MotherBoardHotLine> filterMotherboardsByCpu(List<MotherBoardHotLine> motherBoards, CpuHotLine cpu) {
        return motherBoards.stream()
                .filter(mb -> mb.getSocketType().contains(cpu.getSocketType()))
                .filter(mb -> {
                    if (isIntelCpu(cpu) && cpuContaini5ori7ori9(cpu) && cpu.getName().contains("K")) {
                        return mb.getChipset().charAt(0) == 'Z';
                    } else if (isAmdCpu(cpu) && cpuContainRyzen5or7or9(cpu) && cpu.getName().contains("X")) {
                        return mb.getChipset().charAt(0) == 'X';
                    } else if (cpuContainRyzen5or7or9(cpu) || cpuContaini5ori7ori9(cpu)) {
                        return mb.getChipset().charAt(0) == 'B';
                    } else {
                        return true;
                    }
                })
                .collect(Collectors.toList());
    }

    private boolean isIntelCpu(CpuHotLine cpu) {
        return cpu.getManufacturer() != null && cpu.getManufacturer().contains("Intel");
    }

    private boolean isAmdCpu(CpuHotLine cpu) {
        return cpu.getManufacturer() != null && cpu.getManufacturer().contains("AMD");
    }

    private boolean cpuContainRyzen5or7or9(CpuHotLine cpu) {
        return cpu.getName().contains("Ryzen 5") || cpu.getName().contains("Ryzen 7") || cpu.getName().contains("Ryzen 9");
    }

    private boolean cpuContaini5ori7ori9(CpuHotLine cpu) {
        return cpu.getName().contains("Core i5") || cpu.getName().contains("Core i7") || cpu.getName().contains("Core i9");
    }

    private List<MotherBoardHotLine> updateMotherBoardsList(int propositionQuantityThreshold) {
        return motherBoardHotLineRepository.findMinPriceGroupedByChipsetWithConditions(propositionQuantityThreshold);
    }

    public Integer calculatePriceForFpc(Pc pc) {
        BigDecimal someValue = pc.getPrice();
        BigDecimal denominator = BigDecimal.valueOf(pc.getPredictionGpuFpsFHD());
        if (denominator.compareTo(BigDecimal.ZERO) != 0) {
            return someValue.divide(denominator, RoundingMode.HALF_UP).intValue();
        } else {
            return 0;
        }
    }

    private long measureExecutionTime(Runnable task) {
        long start = System.currentTimeMillis();
        task.run();
        long end = System.currentTimeMillis();
        return end - start;
    }

}
