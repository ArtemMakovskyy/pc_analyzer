package com.example.parser.service.parse.benchmark.user;

import com.example.parser.model.user.benchmark.CpuUserBenchmark;
import com.example.parser.utils.ParseUtil;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserBenchmarkCpuDetailsPageParserFromFile {
    private static final String REGEX_LIVE_ONLY_DIGITS = "\\D";
    private final UserBenchmarkTestPage userBenchmarkTestPage;

    public void purseAndAddDetails(
            CpuUserBenchmark cpu,
            WebDriver driver) {

        userBenchmarkTestPage.checkAndPassTestIfItNeed(driver);
        ParseUtil.addRandomDelayInSeconds(new ParseUtil.DelayInSeconds(5, 10));

        Document document = Jsoup.parse(driver.getPageSource());
        String scoreTableCssSelector = "div.v-center:nth-child(1) > table";
        Elements scoreTableElement = document.select(scoreTableCssSelector);

        String textScoreTable = scoreTableElement
                .select("div.bsc-w.text-left.semi-strong>div:first-of-type").text();

        String[] s = textScoreTable.split(" ");

        double gaming = ParseUtil.stringToDouble(s[1]
                .replaceAll(REGEX_LIVE_ONLY_DIGITS, ""));
        double desktop = ParseUtil.stringToDouble(s[3]
                .replaceAll(REGEX_LIVE_ONLY_DIGITS, ""));
        double workstation = ParseUtil.stringToDouble(s[5]
                .replaceAll(REGEX_LIVE_ONLY_DIGITS, ""));

        cpu.setGamingScore(gaming);
        cpu.setDesktopScore(desktop);
        cpu.setWorkstationScore(workstation);
    }

}
