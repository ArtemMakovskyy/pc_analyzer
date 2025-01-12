package com.example.parser.service.parse.benchmark.user;

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
    private static final int GAMING_SCORE_INDEX1 = 1;
    private static final int DESKTOP_SCORE_INDEX3 = 3;
    private static final int DESKTOP_SCORE_INDEX4 = 4;
    private static final int DESKTOP_SCORE_INDEX5 = 5;
    private static final int WORKSTATION_SCORE_INDEX5 = 5;
    private static final int WORKSTATION_SCORE_INDEX7 = 7;
    private static final int WORKSTATION_SCORE_INDEX9 = 9;
    private static final int CPU_QUANTITY_CORE_INDEX0 = 0;
    private static final int CPU_QUANTITY_THREADS_INDEX2 = 2;
    private static final String CPU_SPECIFICATION_CSS_SELECTOR1 = "p.cmp-cpt";
    private static final String CPU_TABLE_SCORE_SELECTOR2
            = "div.v-center:nth-child(1) > table:nth-child(2) > tbody:nth-child(1) > tr:nth-child(1)";
    private static final String CPU_SCORE_ELEMENTS2 = "div.semi-strong";
    private static final String CPU_SCORE_ELEMENTS3 = "td .bsc-w > div:first-child";
    private static final String REGEX_REMOVE_NON_DIGITS = "\\D";
    private static final String SCORE_ELEMENTS1
            = "div.v-center:nth-child(1) > table:nth-child(2)";
    private static final ParseUtil.DelayInSeconds DELAY_IN_SECONDS
//            = new ParseUtil.DelayInSeconds(3, 6);
                = new ParseUtil.DelayInSeconds(3, 10);
    private final UserBenchmarkTestPage userBenchmarkTestPage;

    public void purseAndAddDetails(
            UserBenchmarkCpu cpu,
            WebDriver driver) {

        Document document = getDocument(driver);

        if (
                parseCpuScore(document, cpu)
                        && parseCpuSpecification(document, cpu)
        ) {
            log.info(cpu);
        } else {
            log.error("Can't parse data from cpu ID: " + cpu.getId());
        }

    }

    private boolean parseCpuScore(Document document, UserBenchmarkCpu cpu) {
        parseCpuScoreTemplate1(document, cpu);
        System.out.println(cpu);
        if (isGetCpuScoreData(cpu)) {
            return true;
        }
        log.info("parseCpuScoreTemplate1 fails");

        parseCpuScoresTemplate2(document, cpu);
        if (isGetCpuScoreData(cpu)) {
            return true;
        }
        log.info("parseCpuScoreTemplate2  fails");

        parseCpuScoresTemplate3(document, cpu);
        if (isGetCpuScoreData(cpu)) {
            return true;
        }

        return false;
    }

    private boolean parseCpuSpecification(Document document, UserBenchmarkCpu cpu) {
        parseCpuSpecificationTemplate1(cpu, document);
        if (isGetCpuSpecificationData(cpu)) {
            return true;
        }
        log.info("parseCpuSpecificationTemplate1 fails");
//        parseCpuSpecificationTemplate2(cpu, document);
        return false;
    }

    private void parseCpuScoresTemplate3(Document document, UserBenchmarkCpu cpu) {
        Elements scoreTableElement = document.select(CPU_TABLE_SCORE_SELECTOR2);
        Elements scoreElements = scoreTableElement.select(CPU_SCORE_ELEMENTS3);

        String[] scorePartsArray = scoreElements.text().split(" ");

        cpu.setGamingScore(
                extractScore(scorePartsArray, GAMING_SCORE_INDEX1));
        cpu.setDesktopScore(
                extractScore(scorePartsArray, DESKTOP_SCORE_INDEX3));
        cpu.setWorkstationScore(
                extractScore(scorePartsArray, WORKSTATION_SCORE_INDEX5));
    }

    private void parseCpuScoresTemplate2(Document document, UserBenchmarkCpu cpu) {
        Elements scoreTableElement = document.select(CPU_TABLE_SCORE_SELECTOR2);
        Elements scoreElements = scoreTableElement.select(CPU_SCORE_ELEMENTS2);

        String[] scorePartsArray = scoreElements.text().split(" ");

        cpu.setGamingScore(
                extractScore(scorePartsArray, GAMING_SCORE_INDEX1));
        cpu.setDesktopScore(
                extractScore(scorePartsArray, DESKTOP_SCORE_INDEX5));
        cpu.setWorkstationScore(
                extractScore(scorePartsArray, WORKSTATION_SCORE_INDEX9));
    }

    private void parseCpuScoreTemplate1(Document document, UserBenchmarkCpu cpu) {
        Elements scoreTableElement = document.select(SCORE_ELEMENTS1);

        String[] scorePartsArray = scoreTableElement.text().split(" ");

        cpu.setGamingScore(
                extractScore(scorePartsArray, GAMING_SCORE_INDEX1));
        cpu.setDesktopScore(
                extractScore(scorePartsArray, DESKTOP_SCORE_INDEX4));
        cpu.setWorkstationScore(
                extractScore(scorePartsArray, WORKSTATION_SCORE_INDEX7));
    }

    private void parseCpuSpecificationTemplate1(UserBenchmarkCpu cpu, Document document) {
        Elements elementCpuSpecification = document.select(CPU_SPECIFICATION_CSS_SELECTOR1);
        String cpuSpecification = elementCpuSpecification.text();
        String[] cpuSpecificationArray = cpuSpecification.split(" ");

        try {
            cpu.setCpuSpecification(cpuSpecification);
            cpu.setCoresQuantity(Integer.parseInt(
                    cpuSpecificationArray[CPU_QUANTITY_CORE_INDEX0]));
            cpu.setThreadsQuantity(Integer.parseInt(
                    cpuSpecificationArray[CPU_QUANTITY_THREADS_INDEX2]));
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            log.warn("Failed to parse CPU specifications. Setting defaults.", e);
            cpu.setCoresQuantity(0);
            cpu.setThreadsQuantity(0);
        }

    }

    private double extractScore(String[] scorePartsArray, int index) {
        if (index >= scorePartsArray.length) {
            return 0;
        }
        try {
            return ParseUtil.stringToDouble(scorePartsArray[index]
                    .replaceAll(REGEX_REMOVE_NON_DIGITS, ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private boolean isGetCpuScoreData(UserBenchmarkCpu cpu) {
        if (cpu.getGamingScore() == null || cpu.getGamingScore() == 0
                || cpu.getDesktopScore() == null || cpu.getDesktopScore() == 0
                || cpu.getWorkstationScore() == null || cpu.getWorkstationScore() == 0
        ) {
            return false;
        }
        return true;
    }


    private boolean isGetCpuSpecificationData(UserBenchmarkCpu cpu) {
        if (cpu.getCoresQuantity() == null || cpu.getCoresQuantity() == 0
                || cpu.getThreadsQuantity() == null || cpu.getThreadsQuantity() == 0
                || cpu.getCpuSpecification() == null || cpu.getCpuSpecification().isBlank()
        ) {
            return false;
        }
        return true;
    }

    private Document getDocument(WebDriver driver) {
        userBenchmarkTestPage.checkAndPassTestIfNecessary(driver);
        ParseUtil.applyRandomDelay(DELAY_IN_SECONDS);
        return Jsoup.parse(driver.getPageSource());
    }

}
