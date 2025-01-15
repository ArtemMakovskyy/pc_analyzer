package com.example.parser.service;

import com.example.parser.model.hotline.CpuHotLine;
import com.example.parser.model.hotline.GpuHotLine;
import com.example.parser.repository.CpuHotLineRepository;
import com.example.parser.repository.GpuHotLineRepository;
import com.example.parser.service.parse.hotline.HotlineCpuPageParser;
import com.example.parser.service.parse.hotline.HotlineGpuPageParser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class HotlineService {
    private final HotlineCpuPageParser hotlineCpuPageParser;
    private final HotlineGpuPageParser hotlineGpuPageParser;
    private final CpuHotLineRepository cpuHotLineRepository;
    private final GpuHotLineRepository gpuHotLineRepository;

    public List<CpuHotLine> parseAndSaveCpus() {
        final List<CpuHotLine> cpusHotLine
                = hotlineCpuPageParser.purseAllPages();
        cpuHotLineRepository.saveAll(cpusHotLine);
        return cpusHotLine;
    }

    public List<GpuHotLine> parseAndSaveGpus() {
        final List<GpuHotLine> gpusHotLine
                = hotlineGpuPageParser.purseAllPages();
        gpuHotLineRepository.saveAll(gpusHotLine);
        return gpusHotLine;
    }

}
