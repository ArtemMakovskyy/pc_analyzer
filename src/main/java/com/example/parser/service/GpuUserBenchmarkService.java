package com.example.parser.service;


import com.example.parser.model.user.benchmark.GpuUserBenchmark;
import com.example.parser.repository.UserBenchmarkGpuRepository;
import com.example.parser.service.parse.benchmark.user.UserBenchmarkGpuPageParser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class GpuUserBenchmarkService {
    private final UserBenchmarkGpuRepository userBenchmarkGpuRepository;
    private final UserBenchmarkGpuPageParser userBenchmarkGpuPageParser;

    public List<GpuUserBenchmark> parseAllAndSaveToDb() {
        //todo check if positions exists
        final List<GpuUserBenchmark> gpuUserBenchmarkList
                = userBenchmarkGpuPageParser.purse();

        final List<GpuUserBenchmark> gpuUserBenchmarks
                = userBenchmarkGpuRepository.saveAll(gpuUserBenchmarkList);

        return gpuUserBenchmarks;
    }

}
