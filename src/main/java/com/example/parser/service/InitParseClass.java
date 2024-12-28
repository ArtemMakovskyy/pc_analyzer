package com.example.parser.service;

import com.example.parser.model.user.benchmark.CpuUserBenchmark;
import com.example.parser.service.artline.ArtLineCpuParser;
import com.example.parser.service.artline.TotalArtLineParser;
import com.example.parser.service.hotline.HotlineCpuPageParser;
import com.example.parser.service.hotline.HotlineGpuPageParser;
import com.example.parser.service.user.benchmark.CpuUserBenchmarkParser;
import com.example.parser.service.user.benchmark.CpuUserBenchmarkParserByPosition;
import jakarta.annotation.PostConstruct;
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
    private final CpuUserBenchmarkParserByPosition cpuUserBenchmarkParserByPosition;

    @PostConstruct
    public void init() {
//        userBenchmark(true);
//        hotline(true);
        artLine(true);
    }

    private void userBenchmark(boolean stat){
        if(stat){
            cpuUserBenchmarkParser.purseAllPages();
//            cpuUserBenchmarkParserByPosition.purseInnerPage(new CpuUserBenchmark());
        }
    }

    private void hotline(boolean stat){
        if(stat){
            hotlineCpuPageParser.purseAllPages();
            hotlineGpuPageParser.purseAllPages();
        }
    }

    private void artLine(boolean stat){
        if(stat) {
            artLineCpuParser.parseAllPagesWithProcessors(false);
//            totalArtLineParser.parseMultiThreaded();
        }
    }
}
