package com.example.parser.service.parse.benchmark.user;

import com.example.parser.dto.userbenchmark.CpuUserBenchmarkCreateDto;
import com.example.parser.model.user.benchmark.UserBenchmarkCpu;
import com.example.parser.utils.ParseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserBenchmarkCpuDetailsPageParser {
    private static final int FIRST_INDEX = 1;
    private static final int THIRD_INDEX = 3;
    private static final int FIFTH_INDEX = 5;
    private static final String REGEX_REMOVE_NON_DIGITS = "\\D";
    private static final String SCORE_TABLE_CSS_SELECTOR
            = "iv.v-center:nth-child(1) > table";
    private static final String SCORE_TABLE_ELEMENT
            = "div.bsc-w.text-left.semi-strong>div:first-of-type";
    private static final ParseUtil.DelayInSeconds DELAY_IN_SECONDS
            = new ParseUtil.DelayInSeconds(5, 10);
    private final UserBenchmarkTestPage userBenchmarkTestPage;

    public void purseAndAddDetails(
            CpuUserBenchmarkCreateDto cpu,
            WebDriver driver) {

        userBenchmarkTestPage.checkAndPassTestIfNecessary(driver);
        ParseUtil.applyRandomDelay(DELAY_IN_SECONDS);

        Document document = Jsoup.parse(driver.getPageSource());
        Elements scoreTableElement = document.select(SCORE_TABLE_CSS_SELECTOR);
        String textScoreTable = scoreTableElement.select(SCORE_TABLE_ELEMENT).text();

        String[] scorePartsArray = textScoreTable.split(" ");

        cpu.setGamingScore(
                extractScore(scorePartsArray, FIRST_INDEX));
        cpu.setDesktopScore(
                extractScore(scorePartsArray, THIRD_INDEX));
        cpu.setWorkstationScore(
                extractScore(scorePartsArray, FIFTH_INDEX));
    }

    private double extractScore(String[] scorePartsArray, int index) {
        return ParseUtil.stringToDouble(scorePartsArray[index]
                .replaceAll(REGEX_REMOVE_NON_DIGITS, ""));
    }

}
