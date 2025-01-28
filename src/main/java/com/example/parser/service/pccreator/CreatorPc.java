package com.example.parser.service.pccreator;

import com.example.parser.model.Pc;
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
import com.example.parser.repository.SsdHotLineRepository;
import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class CreatorPc {
    private final CpuHotLineRepository cpuHotLineRepository;
    private final MotherBoardHotLineRepository motherBoardHotLineRepository;
    private final MemoryHotLineRepository memoryHotLineRepository;
    private final SsdHotLineRepository ssdHotLineRepository;
    private final GpuHotLineRepository gpuHotLineRepository;

//    @PostConstruct
    public void start() {
        final List<CpuHotLine> cpus = cpuHotLineRepository.findMinPriceGroupedByUserBenchmarkCpuId();
        final List<MotherBoardHotLine> motherBoards = motherBoardHotLineRepository.findMinPriceGroupedByChipsetWithConditions();
        final List<MemoryHotLine> memories = memoryHotLineRepository.findMinPriceGroupedByTypeWithConditions();
        final SsdHotLine ssdFromDb = ssdHotLineRepository.findTopByAvgPrice();
        final List<GpuHotLine> gpus = gpuHotLineRepository.findGroupedGpusByMinAvgPrice();
//        cpus.forEach(System.out::println);
//        groupedGpusByMinAvgPrice.forEach(System.out::println);

        createPc(cpus, motherBoards, memories, gpus, ssdFromDb);
    }

    private void createPc(
            List<CpuHotLine> cpus, List<MotherBoardHotLine> motherBoards, List<MemoryHotLine> memories, List<GpuHotLine> gpus, SsdHotLine ssdFromDb) {
        List<Pc> pcs = new ArrayList<>();
        for (CpuHotLine cpu : cpus) {
            //todo 2066 cocket/ Quastion if socket empty
            MotherBoardHotLine motherboard = getMotherboardFromCpuBySocket(motherBoards, cpu);
            MemoryHotLine memory = getMemoryFromMotherBoardBySocketType(memories, motherboard);
            SsdHotLine ssd = ssdFromDb;
            pcs.addAll(
                    video(gpus, cpu, motherboard, memory, ssd)
            );

//            case
//            power supplier

        }
        pcs.forEach(System.out::println);
        System.out.println(pcs.size());

    }

    private List<Pc> video(List<GpuHotLine> gpus, CpuHotLine cpu, MotherBoardHotLine mb, MemoryHotLine memory, SsdHotLine ssd) {
        List<Pc> pcs = new ArrayList<>();

        for (GpuHotLine gpu : gpus) {
            Pc pc = new Pc();

            pc.setGpu(gpu);
            pc.setCpu(cpu);
            pc.setMotherboard(mb);
            pc.setMemory(memory);
            pc.setSsd(ssd);
            //todo implement this
            pc.setPowerSupplier(new PowerSupplierHotLine());
            //todo chenge by enother type then double
            //todo need to add powerSupplier
            double totalPrice = gpu.getAvgPrice() + cpu.getAvgPrice() + mb.getAvgPrice() + memory.getAvgPrice() + ssd.getAvgPrice();
            pc.setPrice(BigDecimal.valueOf(totalPrice));

            pcs.add(pc);
        }
        return pcs;
    }

    private MemoryHotLine getMemoryFromMotherBoardBySocketType(List<MemoryHotLine> memoryHotLineList, MotherBoardHotLine motherBoardHotLine) {
        final String memoryType = motherBoardHotLine.getMemoryType();
        return memoryHotLineList.stream()
                .filter(m -> m.getType().contains(memoryType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No memory found for type: " + memoryType));

    }

    private MotherBoardHotLine getMotherboardFromCpuBySocket(List<MotherBoardHotLine> motherBoards, CpuHotLine cpu) {
        return motherBoards.stream()
                .filter(mb -> mb.getSocketType().contains(cpu.getSocketType()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No motherboard found for CPU: " + cpu.getName() + " with socket type: " + cpu.getSocketType()));
    }


}
