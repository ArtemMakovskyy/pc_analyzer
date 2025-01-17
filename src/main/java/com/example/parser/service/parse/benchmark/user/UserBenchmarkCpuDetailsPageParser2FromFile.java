package com.example.parser.service.parse.benchmark.user;

import com.example.parser.model.user.benchmark.UserBenchmarkCpu;
import com.example.parser.utils.ParseUtil;
import jakarta.annotation.PostConstruct;
import java.io.File;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserBenchmarkCpuDetailsPageParser2FromFile {
    private static final int GAMING_SCORE_INDEX1 = 1;
    private static final int DESKTOP_SCORE_INDEX3 = 3;
    private static final int WORKSTATION_SCORE_INDEX5 = 5;
    private static final String CPU_SPECIFICATION_CSS_SELECTOR1 = "p.cmp-cpt";
    private static final String REGEX_REMOVE_NON_DIGITS = "\\D";
    private static final int CPU_QUANTITY_CORE_INDEX0 = 0;
    private static final int CPU_QUANTITY_THREADS_INDEX2 = 2;

    private static final String CPU_TABLE_SCORE_SELECTOR2
            = "div.v-center:nth-child(1) > table:nth-child(2) > tbody:nth-child(1) > tr:nth-child(1)";

//    @PostConstruct
    //todo improve
    @SneakyThrows
    public void start() {

        UserBenchmarkCpu cpu = new UserBenchmarkCpu();
        Document document = Jsoup.parse(new File("C:\\Users\\Artem\\Documents\\Java\\UltimateJetBrains\\tutorials\\ms1\\parser\\parser\\src\\main\\java\\com\\example\\parser\\service\\parse\\benchmark\\user\\inner.html"));

        purseAndAddDetails(cpu, document);


        if (!isGetCpuScoreData(cpu)) {
            System.out.println(">>>>>>>>>>>>>!!!");
        } else {
            System.out.println(">>>>>>>>>>>>>OOOOOOOOOOOKKKKKKKKKKKKKKK");
        }

        parseCpuSpecificationTemplate1(cpu, document);

        parseCpuSpecificationTemplate2(cpu, document);

        System.out.println(cpu);
    }


    public void purseAndAddDetails(
            UserBenchmarkCpu cpu,
            Document document) {

        final Elements scoreTableElement = document.select(CPU_TABLE_SCORE_SELECTOR2);


        Elements scoreElements = scoreTableElement.select("td .bsc-w > div:first-child");

        String[] scorePartsArray = scoreElements.text().split(" ");

        System.out.println("scorePartsArray");

        for (int i = 0; i < scorePartsArray.length; i++) {
            System.out.println(i + " " + scorePartsArray[i]);
        }

        cpu.setGamingScore(
                extractScore(scorePartsArray, GAMING_SCORE_INDEX1));
        cpu.setDesktopScore(
                extractScore(scorePartsArray, DESKTOP_SCORE_INDEX3));
        cpu.setWorkstationScore(
                extractScore(scorePartsArray, WORKSTATION_SCORE_INDEX5));


        System.out.println(cpu);
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
            cpu.setCoresQuantity(-1);
            cpu.setThreadsQuantity(-1);
        }

    }

    private void parseCpuSpecificationTemplate2(UserBenchmarkCpu cpu, Document document) {
        Elements elementCpuSpecification = document.select(CPU_SPECIFICATION_CSS_SELECTOR1);

        if (elementCpuSpecification.isEmpty()) {
            log.error("parseCpuSpecificationTemplate2(). Element not found!");
            setErrorsCpuValues(cpu);
            return;
        }

        try {
            String textCpuSpecification = elementCpuSpecification.text();

            // Извлечение данных с проверкой на пустоту
            String coresString = textCpuSpecification.replaceAll(".*?(\\d+) Cores.*", "$1");
            String frequencyString = textCpuSpecification.replaceAll(".*@([0-9,.]+) GHz.*", "$1");

            if (coresString.isEmpty() || frequencyString.isEmpty()) {
                throw new IllegalArgumentException("Missing cores or frequency information.");
            }

            double frequency = Double.parseDouble(frequencyString.replace(",", "."));

            // Установка значений в CPU
            cpu.setCoresQuantity(Integer.parseInt(coresString));
            cpu.setThreadsQuantity(0);
            cpu.setCpuSpecification(textCpuSpecification);

        } catch (Exception e) {
            log.error("Error parsing CPU specification: {}", e.getMessage(), e);
            setErrorsCpuValues(cpu);
        }
    }

    private void setErrorsCpuValues(UserBenchmarkCpu cpu) {
        cpu.setCoresQuantity(-1);
        cpu.setThreadsQuantity(-1);
        cpu.setCpuSpecification(null);
    }

    private double extractScore(String[] scorePartsArray, int index) {
        if (index >= scorePartsArray.length) {
            return -1;
        }
        try {
            return ParseUtil.stringToDouble(scorePartsArray[index]
                    .replaceAll(REGEX_REMOVE_NON_DIGITS, ""));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private boolean isGetCpuScoreData(UserBenchmarkCpu cpu) {
        if (cpu.getGamingScore() == null || cpu.getGamingScore() == -1
                || cpu.getDesktopScore() == null || cpu.getDesktopScore() == -1
                || cpu.getWorkstationScore() == null || cpu.getWorkstationScore() == -1
        ) {
            return false;
        }
        return true;
    }

    private boolean isGetCpuSpecificationData(UserBenchmarkCpu cpu) {
        if (cpu.getCoresQuantity() == null || cpu.getCoresQuantity() == -1
                || cpu.getThreadsQuantity() == null || cpu.getThreadsQuantity() == -1
                || cpu.getCpuSpecification() == null || cpu.getCpuSpecification().isBlank()
        ) {
            return false;
        }
        return true;
    }
}
