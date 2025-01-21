package com.example.parser.service.hotline;

import com.example.parser.model.hotline.CpuHotLine;
import com.example.parser.model.hotline.GpuHotLine;
import com.example.parser.model.user.benchmark.UserBenchmarkCpu;
import com.example.parser.model.user.benchmark.UserBenchmarkGpu;
import com.example.parser.repository.CpuHotLineRepository;
import com.example.parser.repository.CpuUserBenchmarkRepository;
import com.example.parser.repository.GpuHotLineRepository;
import com.example.parser.repository.GpuUserBenchmarkRepository;
import com.example.parser.service.parse.hotline.HotlineCpuPageParser;
import com.example.parser.service.parse.hotline.HotlineGpuPageParser;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
public class HotlineUpdaterService {
//    private static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2;
    private static final int THREAD_POOL_SIZE = 16;
    private static final ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    private final HotlineCpuPageParser hotlineCpuPageParser;
    private final CpuHotLineRepository cpuHotLineRepository;
    private final CpuUserBenchmarkRepository cpuUserBenchmarkRepository;
    private final GpuHotLineRepository gpuHotLineRepository;
    private final HotlineGpuPageParser hotlineGpuPageParser;
    private final GpuUserBenchmarkRepository gpuUserBenchmarkRepository;

//    @PostConstruct
    public void parseAll() {
        System.out.println("START parseAll()");
        try {
            List<Future<?>> tasks = List.of(
                    executor.submit(this::parseThenCleanDbThenSaveNewCpuItems),
                    executor.submit(this::parseThenCleanDbThenSaveNewGpuItems)
            );

            // Ждем завершения всех задач
            for (Future<?> task : tasks) {
                try {
                    task.get(); // Блокируем выполнение до завершения каждой задачи
                } catch (InterruptedException | ExecutionException e) {
                    log.error("Error occurred while executing task", e);
                    Thread.currentThread().interrupt();
                }
            }
        } finally {
            shutdownExecutor();
        }
    }

    public List<CpuHotLine> parseThenCleanDbThenSaveNewCpuItems() {
        List<CpuHotLine> cpusHotLine = hotlineCpuPageParser
                .purseAllPagesMultiThread(executor);
        cpuHotLineRepository.deleteAll();
        cpuHotLineRepository.saveAll(cpusHotLine);
        updateCpuWithBenchmarkData();
        return cpusHotLine;
    }

    public List<GpuHotLine> parseThenCleanDbThenSaveNewGpuItems() {
        List<GpuHotLine> gpusHotLine = hotlineGpuPageParser
                .purseAllPagesMultiThread(executor);
        gpuHotLineRepository.deleteAll();
        gpuHotLineRepository.saveAll(gpusHotLine);
        updateGpuWithBenchmarkData();
        return gpusHotLine;
    }

    public void updateCpuWithBenchmarkData() {
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

    public void updateGpuWithBenchmarkData() {
        log.info("Started update gpu scores from User Benchmark DB");
        List<UserBenchmarkGpu> ubGpuSortByModelDesc
                = gpuUserBenchmarkRepository.findAllOrderByModelLengthDesc();
        final List<GpuHotLine> gpuHl = gpuHotLineRepository.findAll();
        for (UserBenchmarkGpu gpuUB : ubGpuSortByModelDesc) {
            for (GpuHotLine gpuHotLine : gpuHl) {
                if (gpuHotLine.getName() != null
                        && gpuHotLine.getUserBenchmarkGpu() == null
                        && gpuHotLine.getName().contains(gpuUB.getModelHl())
                ) {
                    gpuHotLine.setUserBenchmarkGpu(gpuUB);
                }
            }
        }
        gpuHotLineRepository.saveAll(gpuHl);
        log.info("Updated " + gpuHl.size() + " items.");
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
