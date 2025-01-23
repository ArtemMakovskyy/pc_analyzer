package com.example.parser.controller;

import com.example.parser.model.hotline.CpuHotLine;
import com.example.parser.service.hotline.CpuHotlineService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cpu-hotline")
@RequiredArgsConstructor
public class CpuHotlineController {

//    private final CpuHotlineService cpuHotlineService;
//
//    /**
//     * Parse, clean the database, and save new CPU items.
//     *
//     * @param useMultithreading whether to use multithreading for parsing.
//     * @return list of parsed and saved CpuHotLine items.
//     */
//    @PostMapping("/parse-and-save")
//    public ResponseEntity<List<CpuHotLine>> parseThenCleanDbThenSaveNewItems(
//            @RequestParam(defaultValue = "false") boolean useMultithreading) {
//        List<CpuHotLine> result = cpuHotlineService.parseThenCleanDbThenSaveNewItems(useMultithreading);
//        return ResponseEntity.ok(result);
//    }
//
//    /**
//     * Update CPU scores from User Benchmark DB.
//     *
//     * @return ResponseEntity indicating the status of the update process.
//     */
//    @PostMapping("/update-benchmark")
//    public ResponseEntity<String> updateWithBenchmarkData() {
//        cpuHotlineService.updateWithBenchmarkData();
//        return ResponseEntity.ok("CPU benchmark data updated successfully.");
//    }
}