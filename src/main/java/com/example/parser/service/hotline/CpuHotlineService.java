package com.example.parser.service.hotline;

import com.example.parser.model.hotline.CpuHotLine;
import com.example.parser.model.user.benchmark.UserBenchmarkCpu;
import com.example.parser.repository.CpuHotLineRepository;
import com.example.parser.repository.CpuUserBenchmarkRepository;
import com.example.parser.service.parse.hotline.HotlineCpuPageParser;
import jakarta.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    private final HotlineCpuPageParser hotlineCpuPageParser;
    private final CpuHotLineRepository cpuHotLineRepository;
    private final CpuUserBenchmarkRepository cpuUserBenchmarkRepository;
    private static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2;
    private static final ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

//        @PostConstruct
    public void start() {
//        parseThenCleanDbThenSaveNewItems();
        updateWithBenchmarkData();
    }

    public void updateWithBenchmarkData() {
        //todo сделать сортировку перед обновлением как в GpuHotlineService
        log.info("Started update cpu scores from User Benchmark DB");
        List<UserBenchmarkCpu> userBenchmarkCpuList = cpuUserBenchmarkRepository.findAll();
        Set<CpuHotLine> updateList = new HashSet<>();

        for (UserBenchmarkCpu cpu : userBenchmarkCpuList) {
            String modelName = " " + cpu.getModel() + " ";

            List<CpuHotLine> byName
                    = cpuHotLineRepository.findByPartialNameIgnoreCase(modelName);

            for (CpuHotLine cpuHotLine : byName) {
                cpuHotLine.setUserBenchmarkCpu(cpu);
            }
            updateList.addAll(byName);
        }
        cpuHotLineRepository.saveAll(updateList);
        log.info("Updated " + updateList.size() + " items.");
    }

    public List<CpuHotLine> parseThenCleanDbThenSaveNewItems() {
        List<CpuHotLine> cpusHotLine = hotlineCpuPageParser.purseAllPagesMultiThread(executor);
        shutdownExecutor();
        cpuHotLineRepository.deleteAll();
        cpuHotLineRepository.saveAll(cpusHotLine);
        cpusHotLine.forEach(System.out::println);
        return cpusHotLine;
    }

    private void shutdownExecutor() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("Executor did not terminate");
                }
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

}
