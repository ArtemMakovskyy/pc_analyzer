package com.example.parser.service.userbenchmark;


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
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class GpuUserBenchmarkService {
    private final static String PATH = "C:\\Users\\Artem\\Documents\\Java\\UltimateJetBrains\\tutorials\\ms1\\parser\\parser\\src\\main\\resources\\static\\gpu_power_requirements.txt";
    private static final int PARSE_ALL_PAGES_INDEX = -1;
    private static final String REGEX_FIND_POWER = "(\\d+)W";
    private final GpuUserBenchmarkRepository gpuUserBenchmarkRepository;
    private final UserBenchmarkGpuPageParser userBenchmarkGpuPageParser;

    /**
     * Загружает новые данные, фильтрует уже существующие записи и сохраняет новые записи в базу данных.
     *
     * @return Список новых сохраненных записей.
     */
    public List<UserBenchmarkGpu> loadAndSaveNewItems() {
        log.info("Начинаем загрузку и сохранение новых записей GPU User Benchmark.");

        List<UserBenchmarkGpu> parsedGpuList = userBenchmarkGpuPageParser.loadAndParse(true, PARSE_ALL_PAGES_INDEX);
        log.info("Загружено {} записей из источника.", parsedGpuList.size());

        List<UserBenchmarkGpu> existingGpuList = gpuUserBenchmarkRepository.findAll();
        log.info("Найдено {} записей в базе данных.", existingGpuList.size());

        List<UserBenchmarkGpu> newItems = filterNewItems(existingGpuList, parsedGpuList);
        log.info("Обнаружено {} новых записей для сохранения.", newItems.size());

        List<UserBenchmarkGpu> savedItems = gpuUserBenchmarkRepository.saveAll(newItems);
        log.info("Сохранено {} новых записей в базу данных.", savedItems.size());

        return savedItems;
    }

    //1/ todo add to the controller
    public void updateGpuPowerFromFile() {
        List<String> gpuPowerInfoFromFile = readFile(PATH);
        gpuPowerInfoFromFile.sort((s1, s2) -> Integer.compare(s2.length(), s1.length()));
        List<UserBenchmarkGpu> gpus = gpuUserBenchmarkRepository.findAll();
        List<UserBenchmarkGpu> gpusToUpdate = new ArrayList<>();

        for (UserBenchmarkGpu gpu : gpus) {
            for (String gpuPowerInfo : gpuPowerInfoFromFile) {
                if (gpu.getPowerRequirement() == null
                        && gpuPowerInfo.contains(gpu.getModel())) {
                    Integer powerRequirement = extractPowerRequirement(gpuPowerInfo);
                    if (powerRequirement != null) {
                        gpu.setPowerRequirement(powerRequirement);
                        gpusToUpdate.add(gpu);
                        log.info("Updated GPU: " + gpu.getModel() + " with power requirement: " + powerRequirement);
                    } else if (gpuPowerInfo.contains("Интегрированное решение")) {
                        gpu.setPowerRequirement(300);
                        gpusToUpdate.add(gpu);
                        log.info("Updated GPU: " + gpu.getModel() + " with power requirement: 300 (Интегрированное решение)");
                    }
                }
            }
        }

        if (!gpusToUpdate.isEmpty()) {
            gpuUserBenchmarkRepository.saveAll(gpusToUpdate);
        }

    }

    /**
     * Фильтрует новые записи, которые ещё отсутствуют в базе данных.
     *
     * @param existingList Список существующих записей.
     * @param parsedList   Список загруженных записей.
     * @return Список новых записей.
     */
    private static List<UserBenchmarkGpu> filterNewItems(List<UserBenchmarkGpu> existingList, List<UserBenchmarkGpu> parsedList) {
        Set<String> existingModels = existingList.stream()
                .map(UserBenchmarkGpu::getModel)
                .collect(Collectors.toSet());

        return parsedList.stream()
                .filter(parsedItem -> !existingModels.contains(parsedItem.getModel()))
                .collect(Collectors.toList());
    }


    private Integer extractPowerRequirement(String gpuPowerInfo) {
        Pattern pattern = Pattern.compile(REGEX_FIND_POWER);
        Matcher matcher = pattern.matcher(gpuPowerInfo);

        if (matcher.find()) {
            String powerStr = matcher.group(1);
            return Integer.parseInt(powerStr);
        }
        return null;
    }

    private List<String> readFile(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            log.error("Error reading file at path: " + path, e);
            return Collections.emptyList();
        }
    }

}
