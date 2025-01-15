package com.example.parser.controller;

import com.example.parser.model.hotline.CpuHotLine;
import com.example.parser.model.hotline.GpuHotLine;
import com.example.parser.service.HotlineService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hotline")
@RequiredArgsConstructor
public class HotlineController {
    private final HotlineService hotlineService;


    //    @PostConstruct
    public void start() {
    }

    @PostMapping("/cpu")
    public List<CpuHotLine> parseAndSaveAllCpus() {
        //todo chec if items exist
        final List<CpuHotLine> cpuHotLines = hotlineService.parseAndSaveCpus();
        return cpuHotLines;
    }

    @PostMapping("/gpu")
    public List<GpuHotLine> parseAndSaveAllGpus() {
        //todo chec if items exist
        final List<GpuHotLine> gpuHotLines = hotlineService.parseAndSaveGpus();
        return gpuHotLines;
    }

}
