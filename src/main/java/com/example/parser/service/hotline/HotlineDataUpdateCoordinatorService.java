package com.example.parser.service.hotline;

import static com.example.parser.service.parse.hotlinepageparser.impl.HotLinePagesParserAbstract.shutdownExecutor;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
public class HotlineDataUpdateCoordinatorService {
    private final List<DataUpdateService> dataUpdateServices;
    private final List<DatabaseSynchronizationService> databaseSynchronizationServices;

    @PostConstruct
    public void init() {
        updateAllData();
    }

    @SneakyThrows
    public void updateAllData() {
        one();
        two();
        //todo how correct do this
        Thread.sleep(3000);
        shutdownExecutor();
    }

    private void one() {
        log.info("Начало процесса обновления данных для всех сервисов...");

        for (DataUpdateService service : dataUpdateServices) {
            try {
                service.refreshDatabaseWithParsedData();
            } catch (Exception e) {
                log.error("Ошибка при обновлении данных в сервисе {}: {}", service.getClass().getSimpleName(), e.getMessage(), e);
            }
        }
        log.info("Завершено обновление данных для всех сервисов.");
    }

    private void one2() {
        log.info("Начало процесса обновления данных для всех сервисов...");

        for (DataUpdateService service : dataUpdateServices) {
            boolean success = false;
            int attempts = 0;
            while (!success && attempts < 3) {
                try {
                    service.refreshDatabaseWithParsedData();
                    success = true;
                } catch (Exception e) {
                    attempts++;
                    log.error("Ошибка при обновлении данных в сервисе {}: {}. Попытка {}/3",
                            service.getClass().getSimpleName(), e.getMessage(), attempts, e);
                    if (attempts >= 3) {
                        log.error("Ошибка в сервисе {} после 3 попыток: {}",
                                service.getClass().getSimpleName(), e.getMessage(), e);
                    }
                }
            }
        }

        log.info("Завершено обновление данных для всех сервисов.");
    }

    private void two() {
        log.info("Начало процесса обновления данных для всех сервисов...");

        for (DataUpdateService service : dataUpdateServices) {
            try {
                service.refreshDatabaseWithParsedData();
            } catch (Exception e) {
                log.error("Ошибка при обновлении данных в сервисе {}: {}", service.getClass().getSimpleName(), e.getMessage(), e);
            }
        }
        log.info("Завершено обновление данных для всех сервисов.");
    }
}
