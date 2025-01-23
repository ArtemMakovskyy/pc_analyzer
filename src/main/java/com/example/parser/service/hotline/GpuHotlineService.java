package com.example.parser.service.hotline;

import com.example.parser.model.hotline.GpuHotLine;
import com.example.parser.model.user.benchmark.UserBenchmarkGpu;
import com.example.parser.repository.GpuHotLineRepository;
import com.example.parser.repository.GpuUserBenchmarkRepository;
import com.example.parser.service.parse.hotlinepageparser.impl.GpuPageParserImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class GpuHotlineService {
    private final GpuHotLineRepository gpuHotLineRepository;
    private final GpuPageParserImpl hotlineGpuPageParserImpl;
    private final GpuUserBenchmarkRepository gpuUserBenchmarkRepository;


    public List<GpuHotLine> parseThenCleanDbThenSaveNewItems(boolean useMultithreading, String baseUrl) {
        List<GpuHotLine> gpusHotLine;
        if (useMultithreading){
            gpusHotLine = hotlineGpuPageParserImpl.parseAllPagesMultiThread(baseUrl);

        }else {
            gpusHotLine = hotlineGpuPageParserImpl.parse();
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

}
