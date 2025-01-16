package com.example.parser.service;


import com.example.parser.model.user.benchmark.UserBenchmarkGpu;
import com.example.parser.repository.GpuUserBenchmarkRepository;
import com.example.parser.service.parse.benchmark.user.UserBenchmarkGpuPageParser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class GpuUserBenchmarkService {
    private final GpuUserBenchmarkRepository userBenchmarkGpuRepository;
    private final UserBenchmarkGpuPageParser userBenchmarkGpuPageParser;

    public List<UserBenchmarkGpu> parseAllAndSaveToDb() {
        //todo check if positions exists
        final List<UserBenchmarkGpu> gpuUserBenchmarkList
                = userBenchmarkGpuPageParser.purse();

        final List<UserBenchmarkGpu> gpuUserBenchmarks
                = userBenchmarkGpuRepository.saveAll(gpuUserBenchmarkList);

        return gpuUserBenchmarks;
    }

}
