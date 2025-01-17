package com.example.parser.service.hotline;

import com.example.parser.model.hotline.CpuHotLine;
import com.example.parser.model.user.benchmark.UserBenchmarkCpu;
import com.example.parser.repository.CpuHotLineRepository;
import com.example.parser.repository.CpuUserBenchmarkRepository;
import com.example.parser.service.parse.hotline.HotlineCpuPageParser;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

//    @PostConstruct
    public void start() {
        updateWithBenchmarkData();
    }

    public void updateWithBenchmarkData() {
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

    public List<CpuHotLine> parseAndSave() {
        final List<CpuHotLine> cpusHotLine
                = hotlineCpuPageParser.purseAllPages();
        cpuHotLineRepository.saveAll(cpusHotLine);
        return cpusHotLine;
    }

}
