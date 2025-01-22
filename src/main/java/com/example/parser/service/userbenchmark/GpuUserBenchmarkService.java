package com.example.parser.service.userbenchmark;


import com.example.parser.model.user.benchmark.UserBenchmarkGpu;
import com.example.parser.repository.GpuUserBenchmarkRepository;
import com.example.parser.service.parse.benchmark.user.UserBenchmarkGpuPageParser;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class GpuUserBenchmarkService {
    private static final int PARSE_ALL_PAGES_INDEX = -1;
    private final GpuUserBenchmarkRepository gpuUserBenchmarkRepository;
    private final UserBenchmarkGpuPageParser userBenchmarkGpuPageParser;

    /**
     * Загружает новые данные, фильтрует уже существующие записи и сохраняет новые записи в базу данных.
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

    /**
     * Фильтрует новые записи, которые ещё отсутствуют в базе данных.
     * @param existingList Список существующих записей.
     * @param parsedList Список загруженных записей.
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
}
