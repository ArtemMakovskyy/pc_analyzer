package com.example.parser.controller;

import com.example.parser.model.hotline.CpuHotLine;
import com.example.parser.model.hotline.GpuHotLine;
import com.example.parser.service.hotline.CpuHotlineService;
import com.example.parser.service.hotline.GpuHotlineService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hotline")
@RequiredArgsConstructor
public class HotlineController {
    private final CpuHotlineService cpuHotlineService;
    private final GpuHotlineService gpuHotlineService;

    //    @PostConstruct
    public void start() {
    }

    @GetMapping("/cpu")
    public List<CpuHotLine> parseAndSaveAllCpus() {
        //todo chec if items exist
        final List<CpuHotLine> cpuHotLines = cpuHotlineService
                .parseThenCleanDbThenSaveNewItems(true);
        return cpuHotLines;
    }

    @GetMapping("/gpu")
    public List<GpuHotLine> parseAndSaveAllGpus() {
        //todo chec if items exist
        final List<GpuHotLine> gpuHotLines = gpuHotlineService
                .parseThenCleanDbThenSaveNewItems(true);
        return gpuHotLines;
    }

    @GetMapping("/cpu/score")
    public void updateCpuHotlineWithBenchmarkData() {
        cpuHotlineService.updateWithBenchmarkData();
    }

    @GetMapping("/gpu/score")
    public void updateGpuHotlineWithBenchmarkData() {
        gpuHotlineService.updateWithBenchmarkData();
    }

}
