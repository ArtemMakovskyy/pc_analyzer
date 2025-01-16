package com.example.parser.service.hotline;

import com.example.parser.model.hotline.CpuHotLine;
import com.example.parser.model.hotline.GpuHotLine;
import com.example.parser.model.user.benchmark.UserBenchmarkGpu;
import com.example.parser.repository.GpuHotLineRepository;
import com.example.parser.repository.GpuUserBenchmarkRepository;
import com.example.parser.service.parse.hotline.HotlineGpuPageParser;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        updateWithBenchmarkData();
    }

    public void updateWithBenchmarkData() {
//        log.info("Started update gpu scores from User Benchmark DB");
//        List<UserBenchmarkGpu> userBenchmarkGpuList = gpuUserBenchmarkRepository.findAll();
//        Set<GpuHotLine> updateList = new HashSet<>();
//
//        for (UserBenchmarkGpu gpu : userBenchmarkGpuList) {
//            String modelName = " " + gpu.getModel() + " ";
//
//            List<GpuHotLine> byName = cpuHotLineRepository.findByPartialNameIgnoreCase(modelName);
//
//            for (CpuHotLine cpuHotLine : byName) {
//                cpuHotLine.setUserBenchmarkCpu(gpu);
//            }
//            updateList.addAll(byName);
//        }
//        cpuHotLineRepository.saveAll(updateList);
//        log.info("Updated " + updateList.size() + " items.");
    }

    public List<GpuHotLine> parseAndSave() {
        final List<GpuHotLine> gpusHotLine
                = hotlineGpuPageParser.purseAllPages();
        gpuHotLineRepository.saveAll(gpusHotLine);
        return gpusHotLine;
    }

}
