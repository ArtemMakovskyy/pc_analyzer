package com.example.parser.service.hotline;

import com.example.parser.model.hotline.GpuHotLine;
import com.example.parser.model.user.benchmark.UserBenchmarkGpu;
import com.example.parser.repository.GpuHotLineRepository;
import com.example.parser.repository.GpuUserBenchmarkRepository;
import com.example.parser.service.parse.hotlinepageparser.impl.HotlineGpuPageParserImpl;
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
public class GpuHotlineService {
    private final GpuHotLineRepository gpuHotLineRepository;
    private final HotlineGpuPageParserImpl hotlineGpuPageParserImpl;
    private final GpuUserBenchmarkRepository gpuUserBenchmarkRepository;
    private static final int THREAD_POOL_SIZE = 8;
    private static final ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    public List<GpuHotLine> parseThenCleanDbThenSaveNewItems(boolean useMultithreading) {
        List<GpuHotLine> gpusHotLine;
        if (useMultithreading){
            gpusHotLine = hotlineGpuPageParserImpl.parseAllPagesMultiThread(executor);
            shutdownExecutor();
        }else {
            gpusHotLine = hotlineGpuPageParserImpl.parseAllPages();
        }
        gpuHotLineRepository.deleteAll();
        gpuHotLineRepository.saveAll(gpusHotLine);
        updateWithBenchmarkData();
        return gpusHotLine;
    }

    public void updateWithBenchmarkData() {
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
