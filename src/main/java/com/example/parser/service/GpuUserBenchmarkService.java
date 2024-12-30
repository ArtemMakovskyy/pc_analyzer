package com.example.parser.service;


import com.example.parser.model.user.benchmark.GpuUserBenchmark;
import com.example.parser.repository.GpuUserBenchmarkRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class GpuUserBenchmarkService {
    private final GpuUserBenchmarkRepository gpuUserBenchmarkRepository;

    //todo fixed to dto
    public void saveAll(List<GpuUserBenchmark> gpuUserBenchmarks) {
        gpuUserBenchmarkRepository.saveAll(gpuUserBenchmarks);
    }
}
