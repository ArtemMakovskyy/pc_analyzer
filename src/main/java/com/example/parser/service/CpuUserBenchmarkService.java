package com.example.parser.service;

import com.example.parser.dto.mapper.CpuUserBenchmarkMapper;
import com.example.parser.dto.userbenchmark.CpuUserBenchmarkCreateDto;
import com.example.parser.model.user.benchmark.UserBenchmarkCpu;
import com.example.parser.repository.CpuUserBenchmarkRepository;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class CpuUserBenchmarkService {
    private final CpuUserBenchmarkRepository cpuUserBenchmarkRepository;
    private final CpuUserBenchmarkMapper cpuUserBenchmarkMapper;

    public List<UserBenchmarkCpu> saveAll(List<CpuUserBenchmarkCreateDto> createDto) {
        return cpuUserBenchmarkRepository.saveAll(
                createDto.stream()
                        .map(cpuUserBenchmarkMapper::toEntity).toList()
        );
    }

}
