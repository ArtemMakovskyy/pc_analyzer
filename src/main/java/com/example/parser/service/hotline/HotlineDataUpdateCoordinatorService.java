package com.example.parser.service.hotline;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
public class HotlineDataUpdateCoordinatorService {
    private final List<DataUpdateService> dataUpdateServices;
    private final List<DatabaseSynchronizationService> databaseSynchronizationServices;

    public void updateAllData() {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(availableProcessors);

        runAllDataUpdateServices(executor);
        runAllDatabaseSynchronizationServices();

        shutdownExecutor(executor);
    }

    private void runAllDataUpdateServices(ExecutorService executor) {
        log.info("Начало процесса обновления данных для всех сервисов...");

        for (DataUpdateService service : dataUpdateServices) {
            try {
                service.refreshDatabaseWithParsedData(executor);
            } catch (Exception e) {
                log.error("Ошибка при обновлении данных в сервисе {}: {}", service.getClass().getSimpleName(), e.getMessage(), e);
            }
        }
        log.info("Завершено обновление данных для всех сервисов.");
    }

    private void runAllDatabaseSynchronizationServices() {
        log.info("Начало процесса обновления данных для всех сервисов...");

        for (DatabaseSynchronizationService service : databaseSynchronizationServices) {
            try {
                service.synchronizeWithBenchmarkData();
            } catch (Exception e) {
                log.error("Ошибка при обновлении данных в сервисе {}: {}"
                        , service.getClass().getSimpleName(), e.getMessage(), e);
            }
        }
        log.info("Завершено обновление данных для всех сервисов.");

    }

    private void shutdownExecutor(ExecutorService executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    log.error("Executor did not terminate");
                }
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

}
