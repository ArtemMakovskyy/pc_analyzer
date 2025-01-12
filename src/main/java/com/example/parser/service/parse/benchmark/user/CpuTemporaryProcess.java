package com.example.parser.service.parse.benchmark.user;

import com.example.parser.dto.userbenchmark.CpuUserBenchmarkCreateDto;
import com.example.parser.model.user.benchmark.UserBenchmarkCpu;
import com.example.parser.service.CpuUserBenchmarkService;
import com.example.parser.utils.ParseUtil;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class CpuTemporaryProcess {
    private final UserBenchmarkCpuPageParser userBenchmarkCpuPageParser;
    private final UserBenchmarkCpuDetailsPageParser userBenchmarkCpuDetailsPageParser;
    private final CpuUserBenchmarkService cpuUserBenchmarkService;
    private final UserBenchmarkTestPage userBenchmarkTestPage;

    @PostConstruct
    public void start() {
        openCpusWithoutDetailsAndAddDetailsFromUseBenchmark();
    }


    private void openCpusWithoutDetailsAndAddDetailsFromUseBenchmark(){
        cpuUserBenchmarkService.updateSpecificationCpuWereItIsNeed();

    }

    private void openPageAndParseItWeb() {

        WebDriver driver = new ChromeDriver();
        driver.get("https://cpu.userbenchmark.com/Intel-Core-i5-13600K/Rating/4134");
        ParseUtil.applyRandomDelay(3, 10, true);
        userBenchmarkTestPage.checkAndPassTestIfNecessary(driver);
        userBenchmarkCpuDetailsPageParser.purseAndAddDetails(new UserBenchmarkCpu(), driver);

    }


    public void todo() {
        //        check all pauses
        //check code
    }

    private void openCpusFromDbAndAddExtraInfoFromUserBenchmarkIfItNeed() {
        //todo
        // open first, check if data exist
        //in service create open and update
        //  create logic to compare if exist in service
        // If exist - skip
        // if empty go to the user benchmark and parse inner page to find data
    }

    private void parseCpusFromUserBenchmarkAndSaveDataToDb() {
        final List<CpuUserBenchmarkCreateDto> cpusWithoutDetails
                = userBenchmarkCpuPageParser.purse();
        cpusWithoutDetails.forEach(System.out::println);

        cpuUserBenchmarkService.saveAll(cpusWithoutDetails);
    }


}
