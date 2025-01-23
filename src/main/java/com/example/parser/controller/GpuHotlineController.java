package com.example.parser.controller;

import com.example.parser.model.hotline.GpuHotLine;
import com.example.parser.service.hotline.GpuHotlineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gpu-hotline")
@RequiredArgsConstructor
public class GpuHotlineController {

//    private final GpuHotlineService gpuHotlineService;
//
//    /**
//     * Parse, clean the database, and save new GPU items.
//     *
//     * @param useMultithreading whether to use multithreading for parsing.
//     * @return list of parsed and saved GpuHotLine items.
//     */
//    @PostMapping("/parse-and-save")
//    public ResponseEntity<List<GpuHotLine>> parseThenCleanDbThenSaveNewItems(
//            @RequestParam(defaultValue = "false") boolean useMultithreading) {
//        List<GpuHotLine> result = gpuHotlineService.parseThenCleanDbThenSaveNewItems(useMultithreading);
//        return ResponseEntity.ok(result);
//    }
//
//    /**
//     * Update GPU scores from User Benchmark DB.
//     *
//     * @return ResponseEntity indicating the status of the update process.
//     */
//    @PostMapping("/update-benchmark")
//    public ResponseEntity<String> updateWithBenchmarkData() {
//        gpuHotlineService.updateWithBenchmarkData();
//        return ResponseEntity.ok("GPU benchmark data updated successfully.");
//    }
}