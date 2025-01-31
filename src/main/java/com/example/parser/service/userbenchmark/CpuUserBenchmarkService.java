package com.example.parser.service.userbenchmark;

import com.example.parser.dto.mapper.CpuUserBenchmarkMapper;
import com.example.parser.dto.userbenchmark.CpuUserBenchmarkCreateDto;
import com.example.parser.model.user.benchmark.UserBenchmarkCpu;
import com.example.parser.repository.CpuUserBenchmarkRepository;
import com.example.parser.service.parse.WebDriverFactory;
import com.example.parser.service.parse.benchmark.user.UserBenchmarkCpuDetailsPageParser;
import com.example.parser.service.parse.benchmark.user.UserBenchmarkCpuPageParser;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class CpuUserBenchmarkService {
    private final static int FAILURE_VALUE = -1;
    private final static int PROCESS_ALL_PAGES_VALUE = -1;
    private final CpuUserBenchmarkRepository cpuUserBenchmarkRepository;
    private final UserBenchmarkCpuPageParser userBenchmarkCpuPageParser;
    private final UserBenchmarkCpuDetailsPageParser userBenchmarkCpuDetailsPageParser;
    private final CpuUserBenchmarkMapper cpuUserBenchmarkMapper;
    private final WebDriverFactory webDriverFactory;

    //todo add anotherGpuTestData manually

    public List<UserBenchmarkCpu> loadAndSaveNewItems() {

        final List<CpuUserBenchmarkCreateDto> cpuUserBenchmarkCreateDtos =
                userBenchmarkCpuPageParser.loadAndParse(true, PROCESS_ALL_PAGES_VALUE);

        final List<UserBenchmarkCpu> newItems = filterNewItems(cpuUserBenchmarkRepository.findAll(),
                cpuUserBenchmarkCreateDtos
                        .stream()
                        .map(d -> cpuUserBenchmarkMapper.toEntity(d))
                        .toList());

        final List<UserBenchmarkCpu> userBenchmarkCpus
                = saveAllToDb(

                newItems.stream()
                        .map(cpuUserBenchmarkMapper::toDto)
                        .toList()

        );
        log.info("Successfully Was added " + userBenchmarkCpus.size() + " new positions");
        return userBenchmarkCpus;
    }

    public void updateMissingSpecifications() {
        final List<UserBenchmarkCpu> byCpuSpecificationIsNull
                = cpuUserBenchmarkRepository.findByCpuSpecificationIsNull();

        WebDriver driver = webDriverFactory.setUpWebDriver(true, 10);
        int updated = 0;
        int notUpdated = 0;
        int progress = 0;

        try {
            for (UserBenchmarkCpu cpu : byCpuSpecificationIsNull) {
                progress++;
                driver.get(cpu.getUrlOfCpu());
                userBenchmarkCpuDetailsPageParser.purseAndAddDetails(cpu, driver);

                if (isCpuDetailsComplete(cpu)) {
                    log.info(cpu.getId() + " not updated: " + notUpdated++);
                } else {
                    updated++;
                    cpuUserBenchmarkRepository.save(cpu);
                }
                log.info("Progress: " + progress + " from: "
                        + byCpuSpecificationIsNull.size() + " "
                        + ". Updated :" + updated + ". Not updates: " + notUpdated);
            }
        } finally {
            driver.quit();
        }
    }

    private List<UserBenchmarkCpu> saveAllToDb(List<CpuUserBenchmarkCreateDto> createDto) {
        return cpuUserBenchmarkRepository.saveAll(
                createDto.stream()
                        .map(cpuUserBenchmarkMapper::toEntity).toList()
        );
    }

    private static List<UserBenchmarkCpu> filterNewItems(
            List<UserBenchmarkCpu> oldList, List<UserBenchmarkCpu> newList) {

        Set<String> oldNames = oldList.stream()
                .map(UserBenchmarkCpu::getModel)
                .collect(Collectors.toSet());

        return newList.stream()
                .filter(newItem -> !oldNames.contains(newItem.getModel()))
                .collect(Collectors.toList());
    }

    private boolean isCpuDetailsComplete(UserBenchmarkCpu cpu) {
        return cpu.getGamingScore() == null || cpu.getGamingScore() == FAILURE_VALUE
                || cpu.getDesktopScore() == null || cpu.getDesktopScore() == FAILURE_VALUE
                || cpu.getWorkstationScore() == null || cpu.getWorkstationScore() == FAILURE_VALUE
                || cpu.getCoresQuantity() == null || cpu.getCoresQuantity() == FAILURE_VALUE
                || cpu.getThreadsQuantity() == null || cpu.getThreadsQuantity() == FAILURE_VALUE
                || cpu.getCpuSpecification() == null || cpu.getCpuSpecification().isBlank();
    }

}
