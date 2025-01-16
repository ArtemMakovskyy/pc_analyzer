package com.example.parser.service;

import com.example.parser.dto.mapper.CpuUserBenchmarkMapper;
import com.example.parser.dto.userbenchmark.CpuUserBenchmarkCreateDto;
import com.example.parser.model.user.benchmark.UserBenchmarkCpu;
import com.example.parser.repository.CpuHotLineRepository;
import com.example.parser.repository.CpuUserBenchmarkRepository;
import com.example.parser.service.parse.benchmark.user.UserBenchmarkCpuDetailsPageParser;
import com.example.parser.service.parse.benchmark.user.UserBenchmarkCpuPageParser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class CpuUserBenchmarkService {
    private final CpuUserBenchmarkRepository cpuUserBenchmarkRepository;
    private final UserBenchmarkCpuPageParser userBenchmarkCpuPageParser;
    private final UserBenchmarkCpuDetailsPageParser userBenchmarkCpuDetailsPageParser;
    private final CpuUserBenchmarkMapper cpuUserBenchmarkMapper;

    public List<UserBenchmarkCpu> loadAndPurseAndSaveToDb() {
        final List<CpuUserBenchmarkCreateDto> cpuUserBenchmarkCreateDtos =
                userBenchmarkCpuPageParser.loadAndPurseAndSaveToDb();
        final List<UserBenchmarkCpu> userBenchmarkCpus
                = addAllToDb(cpuUserBenchmarkCreateDtos);
        return userBenchmarkCpus;
    }

    public void loadAndParseAndAddSpecificationCpusWereCpuSpecificationIsNull() {
        final List<UserBenchmarkCpu> byCpuSpecificationIsNull
                = cpuUserBenchmarkRepository.findByCpuSpecificationIsNull();

        log.info(byCpuSpecificationIsNull.size());

        WebDriver driver = new ChromeDriver();
        int updated = 0;
        int notUpdated = 0;
        int progress = 0;

        try {
            for (UserBenchmarkCpu cpu : byCpuSpecificationIsNull) {
                progress++;
                driver.get(cpu.getUrlOfCpu());
                userBenchmarkCpuDetailsPageParser.purseAndAddDetails(cpu, driver);

                if (
                        cpu.getGamingScore() == null || cpu.getGamingScore() == 0
                                || cpu.getDesktopScore() == null || cpu.getDesktopScore() == 0
                                || cpu.getWorkstationScore() == null || cpu.getWorkstationScore() == 0
                                || cpu.getCoresQuantity() == null || cpu.getCoresQuantity() == 0
                                || cpu.getThreadsQuantity() == null || cpu.getThreadsQuantity() == 0
                                || cpu.getCpuSpecification() == null || cpu.getCpuSpecification().isBlank()
                ) {
                    log.info(cpu.getId() + " not updated: " + notUpdated++);

                } else {
                    updated++;
                    cpuUserBenchmarkRepository.save(cpu);
                }
                log.info("Progress: " + progress + " from: " + byCpuSpecificationIsNull.size() + " " + ". Updated :" + updated + ". Not updates: " + notUpdated);
            }
        } finally {
            driver.quit();
        }
    }

    private List<UserBenchmarkCpu> addAllToDb(List<CpuUserBenchmarkCreateDto> createDto) {
        return cpuUserBenchmarkRepository.saveAll(
                createDto.stream()
                        .map(cpuUserBenchmarkMapper::toEntity).toList()
        );
    }

}
