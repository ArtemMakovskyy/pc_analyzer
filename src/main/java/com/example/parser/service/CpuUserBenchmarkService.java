package com.example.parser.service;

import com.example.parser.model.user.benchmark.CpuUserBenchmark;
import com.example.parser.repository.CpuUserBenchmarkRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class CpuUserBenchmarkService {
    private final CpuUserBenchmarkRepository cpuUserBenchmarkRepository;

    //todo fixed to dto
    public void saveAll(List<CpuUserBenchmark> createDto){
        cpuUserBenchmarkRepository.saveAll(createDto);
    }
}
