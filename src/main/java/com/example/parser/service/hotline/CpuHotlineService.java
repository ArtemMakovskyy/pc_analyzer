package com.example.parser.service.hotline;

import com.example.parser.exseption.CustomServiceException;
import com.example.parser.model.hotline.CpuHotLine;
import com.example.parser.model.user.benchmark.UserBenchmarkCpu;
import com.example.parser.repository.CpuHotLineRepository;
import com.example.parser.repository.CpuUserBenchmarkRepository;
import com.example.parser.service.parse.MultiThreadPagesParser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class CpuHotlineService {
    private final MultiThreadPagesParser<CpuHotLine> сpuPageParserImpl;
    private final CpuHotLineRepository cpuHotLineRepository;
    private final CpuUserBenchmarkRepository cpuUserBenchmarkRepository;

    @Transactional
    public List<CpuHotLine> parseThenCleanDbThenSaveNewItems() {
        try {
            log.info("Starting cpu data update process...");
            List<CpuHotLine> cpusHotLine = сpuPageParserImpl.parseAllMultiThread();

            log.info("Parsed {} cpus.", cpusHotLine.size());
            cpuHotLineRepository.deleteAll();
            log.info("Deleted old cpu data.");

            final List<CpuHotLine> cpuHotLinesFromDb = cpuHotLineRepository.saveAll(cpusHotLine);
            log.info("Saved {} new cpu records.", cpuHotLinesFromDb.size());
            return cpuHotLinesFromDb;
        } catch (Exception e) {
            log.error("Error occurred during cpu data update process: {}", e.getMessage(), e);
            throw new CustomServiceException("Failed to process cpu data", e);
        }
    }

    @Transactional
    public void updateWithBenchmarkData() {
        log.info("Started update cpu scores from User Benchmark DB");
        List<UserBenchmarkCpu> ubCpuSortByModelDesc
                = cpuUserBenchmarkRepository.findAllOrderByModelLengthDesc();
        final List<CpuHotLine> cpuHL = cpuHotLineRepository.findAll();

        for (UserBenchmarkCpu cpuUB : ubCpuSortByModelDesc) {
            for (CpuHotLine cpuHotLine : cpuHL) {
                if (cpuHotLine.getName() != null
                        && cpuHotLine.getUserBenchmarkCpu() == null
                        && cpuUB.getModelHl() != null
                        && cpuHotLine.getName().contains(cpuUB.getModelHl())
                ) {
                    cpuHotLine.setUserBenchmarkCpu(cpuUB);
                }
            }
        }
        cpuHotLineRepository.saveAll(cpuHL);
        log.info("Updated " + cpuHL.size() + " items.");
    }

}
