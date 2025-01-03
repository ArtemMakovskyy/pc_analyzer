package com.example.parser.service.parse;

import com.example.parser.model.user.benchmark.CpuUserBenchmark;

import com.example.parser.service.CpuHotlineService;
import com.example.parser.service.CpuUserBenchmarkService;
import com.example.parser.service.GpuHotlineService;
import com.example.parser.service.GpuUserBenchmarkService;
import com.example.parser.service.parse.artline.ArtLineCpuParser;
import com.example.parser.service.parse.artline.TotalArtLineParser;
import com.example.parser.service.parse.benchmark.user.CpuUserBenchmarkParser;
import com.example.parser.service.parse.benchmark.user.GpuUserBenchmarkParserWithGsoup;
import com.example.parser.service.parse.hotline.HotlineCpuPageParser;
import com.example.parser.service.parse.hotline.HotlineGpuPageParser;
import com.example.parser.service.parse.pda.PdaJsoupParser;
import com.example.parser.service.parse.pda.PdaSeleniumParser;
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
    private final GpuUserBenchmarkParserWithGsoup gpuUserBenchmarkParserWithGsoup;
    private final PdaJsoupParser pdaJsoupParser;
    private final PdaSeleniumParser pdaSeleniumParser;

    private final CpuHotlineService cpuHotlineService;
    private final GpuHotlineService gpuHotlineService;

    private final CpuUserBenchmarkService cpuUserBenchmarkService;
    private final GpuUserBenchmarkService gpuUserBenchmarkService;


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
//        cpuUserBenchmarkParser.purseAllPages().forEach(System.out::println);
//        gpuUserBenchmarkParser.purseAllPages().forEach(System.out::println);
//        cpuUserBenchmarkService.saveAll(cpuUserBenchmarkParser.purseAllPages());
//        gpuUserBenchmarkService.saveAll(gpuUserBenchmarkParser.purseAllPages());
    }

    private void hotline() {
//        hotlineCpuPageParser.purseAllPages();
//        hotlineGpuPageParser.purseAllPages();

        cpuHotlineService.saveAll(hotlineCpuPageParser.purseAllPages());
//        gpuHotlineService.saveAll(hotlineGpuPageParser.purseAllPages());
    }

    private void artLine() {
//            artLineCpuParser.parseAllPagesWithProcessors(false);
        totalArtLineParser.parseMultiThreaded();
    }

    private void pda() {
        pdaJsoupParser.parse();
    }
}
