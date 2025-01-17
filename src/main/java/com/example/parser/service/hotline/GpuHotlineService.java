package com.example.parser.service.hotline;

import com.example.parser.model.hotline.GpuHotLine;
import com.example.parser.model.user.benchmark.UserBenchmarkGpu;
import com.example.parser.repository.GpuHotLineRepository;
import com.example.parser.repository.GpuUserBenchmarkRepository;
import com.example.parser.service.parse.hotline.HotlineGpuPageParser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class GpuHotlineService {
    private final GpuHotLineRepository gpuHotLineRepository;
    private final HotlineGpuPageParser hotlineGpuPageParser;
    private final GpuUserBenchmarkRepository gpuUserBenchmarkRepository;

    //    @PostConstruct
    public void start() {
    }

    public void updateWithBenchmarkData() {
        log.info("Started update gpu scores from User Benchmark DB");
        List<UserBenchmarkGpu> ubPpuSortByModelDesc
                = gpuUserBenchmarkRepository.findAllOrderByModelLengthDesc();
        final List<GpuHotLine> gpuHl = gpuHotLineRepository.findAll();
        for (UserBenchmarkGpu gpuUB : ubPpuSortByModelDesc) {
            for (GpuHotLine gpuHL : gpuHl) {
                if (gpuHL.getName() != null
                        && gpuHL.getUserBenchmarkGpu() == null
                        && gpuHL.getName().contains(gpuUB.getModelHl())
                ) {
                    gpuHL.setUserBenchmarkGpu(gpuUB);
                }
            }
        }
        gpuHotLineRepository.saveAll(gpuHl);
        log.info("Updated " + gpuHl.size() + " items.");
    }

    public List<GpuHotLine> parseThenCleanDbThenSaveNewItems() {
        List<GpuHotLine> gpusHotLine = hotlineGpuPageParser.purseAllPages();
        gpuHotLineRepository.deleteAll();
        gpuHotLineRepository.saveAll(gpusHotLine);

        return gpusHotLine;
    }

}
