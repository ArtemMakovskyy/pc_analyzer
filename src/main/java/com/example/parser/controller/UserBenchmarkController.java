package com.example.parser.controller;

import com.example.parser.model.user.benchmark.UserBenchmarkGpu;
import com.example.parser.model.user.benchmark.UserBenchmarkCpu;
import com.example.parser.service.userbenchmark.CpuUserBenchmarkService;
import com.example.parser.service.userbenchmark.GpuUserBenchmarkService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userbenchmark")
@RequiredArgsConstructor
public class UserBenchmarkController {
    private final CpuUserBenchmarkService userBenchmarkService;
    private final GpuUserBenchmarkService gpuUserBenchmarkService;

//        @PostConstruct
    public void start() {

    }

    @PostMapping("/purse/cpu")
    public List<UserBenchmarkCpu> loadAndParseAndSaveCpusWithoutDetails() {
        final List<UserBenchmarkCpu> userBenchmarkCpus
                = userBenchmarkService.loadAndParseAndSaveToDb();
        return userBenchmarkCpus;
    }

    @PutMapping("/purse/cpu")
    public void loadAndParseAndAddSpecificationCpusWereCpuSpecificationIsNull() {
        userBenchmarkService.loadAndParseAndAddSpecificationCpusWereCpuSpecificationIsNull();
    }

    @GetMapping("/purse/gpu")
    public List<UserBenchmarkGpu> loadAndParseAndSaveGpus() {
        //todo check if exists data
        final List<UserBenchmarkGpu> gpus
                = gpuUserBenchmarkService.parseAllAndSaveToDb();
        return gpus;
    }

}
