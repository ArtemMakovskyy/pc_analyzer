package com.example.parser.service.hotline;

import com.example.parser.exseption.CustomServiceException;
import com.example.parser.model.hotline.CpuHotLine;
import com.example.parser.model.hotline.GpuHotLine;
import com.example.parser.model.user.benchmark.UserBenchmarkCpu;
import com.example.parser.model.user.benchmark.UserBenchmarkGpu;
import com.example.parser.repository.GpuHotLineRepository;
import com.example.parser.repository.GpuUserBenchmarkRepository;
import com.example.parser.service.parse.MultiThreadPageParser;
import com.example.parser.service.parse.hotlinepageparser.impl.GpuPageParserImpl;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class GpuHotlineService {
    private final MultiThreadPageParser<GpuHotLine> gpuPageParserImpl;
    private final GpuHotLineRepository gpuHotLineRepository;
    private final GpuUserBenchmarkRepository gpuUserBenchmarkRepository;

        //todo working implement service
//    @PostConstruct
//    public void init() {
//        parseThenCleanDbThenSaveNewItems().forEach(System.out::println);
//        updateWithBenchmarkData();
//    }

    @Transactional
    public List<GpuHotLine> parseThenCleanDbThenSaveNewItems() {
        try {
            log.info("Starting gpu data update process...");
            List<GpuHotLine> gpusHotLine = gpuPageParserImpl.parseMultiThread();

            log.info("Parsed {} gpus.", gpusHotLine.size());
            gpuHotLineRepository.deleteAll();
            log.info("Deleted old gpu data.");

            gpuHotLineRepository.saveAll(gpusHotLine);
            log.info("Saved {} new gpu records.", gpusHotLine.size());
            return gpusHotLine;
        } catch (Exception e) {
            log.error("Error occurred during gpu data update process: {}", e.getMessage(), e);
            throw new CustomServiceException("Failed to process gpu data", e);
        }
    }

    @Transactional
    public void updateWithBenchmarkData() {
        log.info("Started update gpu scores from User Benchmark DB");
        List<UserBenchmarkGpu> ubGpuSortByModelDesc
                = gpuUserBenchmarkRepository.findAllOrderByModelLengthDesc();
        List<GpuHotLine> gpuHl = gpuHotLineRepository.findAll();

        for (UserBenchmarkGpu gpuUB : ubGpuSortByModelDesc) {
            for (GpuHotLine gpuHotLine : gpuHl) {
                if (gpuHotLine.getName() != null
                        && gpuHotLine.getUserBenchmarkGpu() == null
                        && gpuUB.getModelHl() != null
                        && gpuHotLine.getName().contains(gpuUB.getModelHl())
                ) {
                    gpuHotLine.setUserBenchmarkGpu(gpuUB);
                }
            }
        }
        gpuHotLineRepository.saveAll(gpuHl);
        log.info("Updated " + gpuHl.size() + " items.");
    }

}
