package com.example.parser.controller;

import com.example.parser.service.userbenchmark.CpuUserBenchmarkService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-benchmark/cpu")
@RequiredArgsConstructor
public class CpuUserBenchmarkController {
    private final CpuUserBenchmarkService cpuUserBenchmarkService;

    @PostConstruct
    public void init(){
//        updateAllCpus();
        updateSpecifications();
    }

    @PostMapping("/update-all")
    public ResponseEntity<String> updateAllCpus() {
        cpuUserBenchmarkService.loadAndSaveNewItems();
        return ResponseEntity.ok("CPU benchmarks successfully updated.");
    }

    @PostMapping("/update-specifications")
    public ResponseEntity<String> updateSpecifications() {
        cpuUserBenchmarkService.updateMissingSpecifications();
        return ResponseEntity.ok("CPU specifications updated for missing items.");
    }
}
