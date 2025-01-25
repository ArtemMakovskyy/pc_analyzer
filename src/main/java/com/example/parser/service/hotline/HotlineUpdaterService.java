package com.example.parser.service.hotline;

import com.example.parser.repository.CpuHotLineRepository;
import com.example.parser.repository.CpuUserBenchmarkRepository;
import com.example.parser.repository.GpuHotLineRepository;
import com.example.parser.repository.GpuUserBenchmarkRepository;
import com.example.parser.service.parse.hotlinepageparser.impl.CpuHotLinePagesParserImpl;
import com.example.parser.service.parse.hotlinepageparser.impl.GpuHotLinePagesParserImpl;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
public class HotlineUpdaterService {
    private static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private static final ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    private final CpuHotLinePagesParserImpl hotlineCpuHotLinePageParserImpl;
    private final CpuHotLineRepository cpuHotLineRepository;
    private final CpuUserBenchmarkRepository cpuUserBenchmarkRepository;
    private final GpuHotLineRepository gpuHotLineRepository;
    private final GpuHotLinePagesParserImpl hotlineGpuHotLinePageParserImpl;
    private final GpuUserBenchmarkRepository gpuUserBenchmarkRepository;

//    public void parseAllMT() {
//
//        try {
//            List<Future<?>> tasks = List.of(
//                    executor.submit(this::parseThenCleanDbThenSaveNewCpuItems),
//                    executor.submit(this::parseThenCleanDbThenSaveNewGpuItems)
//            );
//
//            for (Future<?> task : tasks) {
//                try {
//                    task.get();
//                } catch (InterruptedException | ExecutionException e) {
//                    log.error("Error occurred while executing task", e);
//                    Thread.currentThread().interrupt();
//                }
//            }
//        } finally {
//
//        }
//    }
//
//    private List<CpuHotLine> parseThenCleanDbThenSaveNewCpuItems() {
//        List<CpuHotLine> cpusHotLine = hotlineCpuPageParserImpl
//                .parseMultiThread();
//        cpuHotLineRepository.deleteAll();
//        cpuHotLineRepository.saveAll(cpusHotLine);
//        updateCpuWithBenchmarkData();
//        return cpusHotLine;
//    }
//
//    private List<GpuHotLine> parseThenCleanDbThenSaveNewGpuItems() {
//        List<GpuHotLine> gpusHotLine = hotlineGpuPageParserImpl
//                .parseAllPagesMultiThread("");
//        gpuHotLineRepository.deleteAll();
//        gpuHotLineRepository.saveAll(gpusHotLine);
//        updateGpuWithBenchmarkData();
//        return gpusHotLine;
//    }
//
//    private void updateCpuWithBenchmarkData() {
//        log.info("Started update cpu scores from User Benchmark DB");
//        List<UserBenchmarkCpu> ubCpuSortByModelDesc
//                = cpuUserBenchmarkRepository.findAllOrderByModelLengthDesc();
//        final List<CpuHotLine> cpuHL = cpuHotLineRepository.findAll();
//
//        for (UserBenchmarkCpu cpuUB : ubCpuSortByModelDesc) {
//            for (CpuHotLine cpuHotLine : cpuHL) {
//                if (cpuHotLine.getName() != null
//                        && cpuHotLine.getUserBenchmarkCpu() == null
//                        && cpuHotLine.getName().contains(cpuUB.getModelHl())
//                ) {
//                    cpuHotLine.setUserBenchmarkCpu(cpuUB);
//                }
//            }
//        }
//        cpuHotLineRepository.saveAll(cpuHL);
//        log.info("Updated " + cpuHL.size() + " items.");
//    }
//
//    private void updateGpuWithBenchmarkData() {
//        log.info("Started update gpu scores from User Benchmark DB");
//        List<UserBenchmarkGpu> ubGpuSortByModelDesc
//                = gpuUserBenchmarkRepository.findAllOrderByModelLengthDesc();
//        final List<GpuHotLine> gpuHl = gpuHotLineRepository.findAll();
//        for (UserBenchmarkGpu gpuUB : ubGpuSortByModelDesc) {
//            for (GpuHotLine gpuHotLine : gpuHl) {
//                if (gpuHotLine.getName() != null
//                        && gpuHotLine.getUserBenchmarkGpu() == null
//                        && gpuHotLine.getName().contains(gpuUB.getModelHl())
//                ) {
//                    gpuHotLine.setUserBenchmarkGpu(gpuUB);
//                }
//            }
//        }
//        gpuHotLineRepository.saveAll(gpuHl);
//        log.info("Updated " + gpuHl.size() + " items.");
//    }

}
