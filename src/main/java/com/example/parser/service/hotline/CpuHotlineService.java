package com.example.parser.service.hotline;

import com.example.parser.model.hotline.CpuHotLine;
import com.example.parser.model.user.benchmark.UserBenchmarkCpu;
import com.example.parser.repository.CpuHotLineRepository;
import com.example.parser.repository.CpuUserBenchmarkRepository;
import com.example.parser.service.parse.hotlinepageparser.impl.HotlineCpuPageParserImpl;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class CpuHotlineService {
    private final HotlineCpuPageParserImpl hotlineCpuPageParserImpl;
    private final CpuHotLineRepository cpuHotLineRepository;
    private final CpuUserBenchmarkRepository cpuUserBenchmarkRepository;


    public List<CpuHotLine> parseThenCleanDbThenSaveNewItems(boolean useMultithreading) {
        List<CpuHotLine> cpusHotLine;
        if (useMultithreading){
            cpusHotLine = hotlineCpuPageParserImpl.parseAllPagesMultiThread();
        }else {
           cpusHotLine = hotlineCpuPageParserImpl.parseAllPages();
        }
        cpuHotLineRepository.deleteAll();
        cpuHotLineRepository.saveAll(cpusHotLine);
        updateWithBenchmarkData();
        return cpusHotLine;
    }

    public void updateWithBenchmarkData() {
        log.info("Started update cpu scores from User Benchmark DB");
        List<UserBenchmarkCpu> ubCpuSortByModelDesc
                = cpuUserBenchmarkRepository.findAllOrderByModelLengthDesc();
        final List<CpuHotLine> cpuHL = cpuHotLineRepository.findAll();

        for (UserBenchmarkCpu cpuUB : ubCpuSortByModelDesc) {
            for (CpuHotLine cpuHotLine : cpuHL) {
                if (cpuHotLine.getName() != null
                        && cpuHotLine.getUserBenchmarkCpu() == null
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
