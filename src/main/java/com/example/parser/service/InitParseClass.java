package com.example.parser.service;

import com.example.parser.model.user.benchmark.CpuUserBenchmark;
import com.example.parser.service.artline.ArtLineCpuParser;
import com.example.parser.service.artline.TotalArtLineParser;
import com.example.parser.service.hotline.HotlineCpuPageParser;
import com.example.parser.service.hotline.HotlineGpuPageParser;
import com.example.parser.service.pda.PdaJsoupParser;
import com.example.parser.service.pda.PdaSeleniumParser;
import com.example.parser.service.user.benchmark.CpuUserBenchmarkParser;
import com.example.parser.service.user.benchmark.GpuUserBenchmarkParser;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class InitParseClass {
    private final HotlineCpuPageParser hotlineCpuPageParser;
    private final HotlineGpuPageParser hotlineGpuPageParser;
    private final ArtLineCpuParser artLineCpuParser;
    private final TotalArtLineParser totalArtLineParser;
    private final CpuUserBenchmarkParser cpuUserBenchmarkParser;
    private final GpuUserBenchmarkParser gpuUserBenchmarkParser;
    private final PdaJsoupParser pdaJsoupParser;
    private final PdaSeleniumParser pdaSeleniumParser;

    @PostConstruct
    public void init() {
        ExecutorService executor = Executors.newFixedThreadPool(6);
//        executor.submit(this::userBenchmark);
//        executor.submit(this::hotline);
//        executor.submit(this::artLine);
//        executor.submit(this::pda);
//
        executor.shutdown();

//        userBenchmark();
    }

    private void userBenchmark() {
        final List<CpuUserBenchmark> cpuUserBenchmarks = cpuUserBenchmarkParser.purseAllPages();
        cpuUserBenchmarkParser.purseAllPages().forEach(System.out::println);
        gpuUserBenchmarkParser.purseAllPages().forEach(System.out::println);
    }

    private void hotline() {
        hotlineCpuPageParser.purseAllPages();
        hotlineGpuPageParser.purseAllPages();
    }

    private void artLine() {
//            artLineCpuParser.parseAllPagesWithProcessors(false);
        totalArtLineParser.parseMultiThreaded();
    }

    private void pda() {
        pdaJsoupParser.parse();
    }
}
