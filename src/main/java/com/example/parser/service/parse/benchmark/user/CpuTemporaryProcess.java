package com.example.parser.service.parse.benchmark.user;

import com.example.parser.dto.userbenchmark.CpuUserBenchmarkCreateDto;
import com.example.parser.service.CpuUserBenchmarkService;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class CpuTemporaryProcess {
    private final UserBenchmarkCpuPageParser userBenchmarkCpuPageParser;
    private final UserBenchmarkCpuDetailsPageParser userBenchmarkCpuDetailsPageParser;
    private final CpuUserBenchmarkService cpuUserBenchmarkService;

    @PostConstruct
    public void start() {
        final List<CpuUserBenchmarkCreateDto> cpusWithoutDetails
                = userBenchmarkCpuPageParser.purse();
        cpusWithoutDetails.forEach(System.out::println);

        cpuUserBenchmarkService.saveAll(cpusWithoutDetails);
    }
}
