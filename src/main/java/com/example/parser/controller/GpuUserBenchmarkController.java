package com.example.parser.controller;

import com.example.parser.model.user.benchmark.UserBenchmarkGpu;
import com.example.parser.service.userbenchmark.GpuUserBenchmarkService;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gpu-benchmark")
@RequiredArgsConstructor
@Log4j2
public class GpuUserBenchmarkController {
    private final GpuUserBenchmarkService gpuUserBenchmarkService;

    /**
     * Загружает и сохраняет новые записи GPU User Benchmark.
     * @return Список новых сохраненных записей.
     */
    @PostMapping("/load-and-save")
    public ResponseEntity<List<UserBenchmarkGpu>> loadAndSaveNewItems() {
        log.info("Обработка запроса на загрузку и сохранение новых записей.");
        List<UserBenchmarkGpu> savedItems = gpuUserBenchmarkService.loadAndSaveNewItems();
        return ResponseEntity.ok(savedItems);
    }

}
