package com.example.parser.service.userbenchmark;


import com.example.parser.dto.mapper.GpuUserBenchmarkMapper;
import com.example.parser.dto.userbenchmark.GpuUserBenchmarkParserDto;
import com.example.parser.model.user.benchmark.UserBenchmarkGpu;
import com.example.parser.repository.GpuUserBenchmarkRepository;
import com.example.parser.service.parse.benchmark.user.UserBenchmarkGpuPageParser;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class GpuUserBenchmarkService {
//    private final static String PATH = "C:\\Users\\Artem\\Documents\\Java\\UltimateJetBrains\\tutorials\\ms1\\parser\\parser\\src\\main\\resources\\static\\gpu_power_requirements.txt";
    private static final int PROCESS_ALL_PAGES_VALUE_DEFAULT = -1;
//    private static final int DEFAULT_POWER = 300;
    @Value("${parse.pages.quantity.gpu.user.benchmark}")
    private int parsePagesQuantity;
    @Value("${sort.by.age.selenium}")
    private boolean sortByAge;
//    private static final String REGEX_FIND_POWER = "(\\d+)W";
    private final GpuUserBenchmarkRepository gpuUserBenchmarkRepository;
    private final UserBenchmarkGpuPageParser userBenchmarkGpuPageParser;
    private final GpuUserBenchmarkMapper gpuUserBenchmarkMapper;

    @PostConstruct
    public List<UserBenchmarkGpu> getAllWerePowerRequirementIsNull(){
        final List<UserBenchmarkGpu> allOrderByModelLengthDesc
                = gpuUserBenchmarkRepository.findAllByPowerRequirementIsNull();
        allOrderByModelLengthDesc.forEach(System.out::println);
        return allOrderByModelLengthDesc;
    }



    public List<UserBenchmarkGpu> loadAndSaveNewItems() {
        int parsePages = parsePagesQuantity != 0 ? parsePagesQuantity : PROCESS_ALL_PAGES_VALUE_DEFAULT;

        log.info("Starting the loading and saving of new GPU User Benchmark records.");

        List<GpuUserBenchmarkParserDto> parsedGpuList = userBenchmarkGpuPageParser
                .loadAndParse(sortByAge, parsePages);
        log.info("Loaded {} records from the source.", parsedGpuList.size());

        List<UserBenchmarkGpu> existingGpuList = gpuUserBenchmarkRepository.findAll();
        log.info("Found {} records in the database.", existingGpuList.size());

        List<UserBenchmarkGpu> userBenchmarkGpus = parsedGpuList.stream()
                .map(gpuUserBenchmarkMapper::toEntity)
                .toList();

        List<UserBenchmarkGpu> newItems = filterNewItems(existingGpuList, userBenchmarkGpus);
        log.info("Detected {} new records for saving.", newItems.size());

        List<UserBenchmarkGpu> savedItems = gpuUserBenchmarkRepository.saveAll(newItems);
        log.info("Saved {} new records to the database.", savedItems.size());

        return savedItems;
    }


//    public void updateGpuPowerFromFile() {
//        List<String> gpuPowerInfoFromFile = readFile(PATH);
//        gpuPowerInfoFromFile.sort((s1, s2) -> Integer.compare(s2.length(), s1.length()));
//        List<UserBenchmarkGpu> gpus = gpuUserBenchmarkRepository.findAll();
//        List<UserBenchmarkGpu> gpusToUpdate = new ArrayList<>();
//
//        for (UserBenchmarkGpu gpu : gpus) {
//            for (String gpuPowerInfo : gpuPowerInfoFromFile) {
//                if (gpu.getPowerRequirement() == null
//                        && gpuPowerInfo.contains(gpu.getModel())) {
//                    Integer powerRequirement = extractPowerRequirement(gpuPowerInfo);
//                    if (powerRequirement != null) {
//                        gpu.setPowerRequirement(powerRequirement);
//                        gpusToUpdate.add(gpu);
//                        log.info("Updated GPU: " + gpu.getModel() + " with power requirement: " + powerRequirement);
//                    } else if (gpuPowerInfo.contains("Интегрированное решение")) {
//                        gpu.setPowerRequirement(DEFAULT_POWER);
//                        gpusToUpdate.add(gpu);
//                        log.info("Updated GPU: " + gpu.getModel() + " with power requirement: 300 (Интегрированное решение)");
//                    }
//                }
//            }
//        }
//
//        if (!gpusToUpdate.isEmpty()) {
//            gpuUserBenchmarkRepository.saveAll(gpusToUpdate);
//        }
//
//    }

    private static List<UserBenchmarkGpu> filterNewItems(List<UserBenchmarkGpu> existingList, List<UserBenchmarkGpu> parsedList) {
        Set<String> existingModels = existingList.stream()
                .map(UserBenchmarkGpu::getModel)
                .collect(Collectors.toSet());

        return parsedList.stream()
                .filter(parsedItem -> !existingModels.contains(parsedItem.getModel()))
                .collect(Collectors.toList());
    }


//    private Integer extractPowerRequirement(String gpuPowerInfo) {
//        Pattern pattern = Pattern.compile(REGEX_FIND_POWER);
//        Matcher matcher = pattern.matcher(gpuPowerInfo);
//
//        if (matcher.find()) {
//            String powerStr = matcher.group(1);
//            return Integer.parseInt(powerStr);
//        }
//        return null;
//    }
//
//    private List<String> readFile(String path) {
//        try {
//            return Files.readAllLines(Path.of(path));
//        } catch (IOException e) {
//            log.error("Error reading file at path: " + path, e);
//            return Collections.emptyList();
//        }
//    }

}
