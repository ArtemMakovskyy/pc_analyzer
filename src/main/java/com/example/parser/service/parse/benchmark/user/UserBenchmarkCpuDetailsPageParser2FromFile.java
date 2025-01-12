package com.example.parser.service.parse.benchmark.user;

import com.example.parser.model.user.benchmark.UserBenchmarkCpu;
import com.example.parser.utils.ParseUtil;
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
    @SneakyThrows
    public void start() {

        UserBenchmarkCpu cpu = new UserBenchmarkCpu();
        Document document = Jsoup.parse(new File("C:\\Users\\Artem\\Documents\\Java\\UltimateJetBrains\\tutorials\\ms1\\parser\\parser\\src\\main\\java\\com\\example\\parser\\service\\parse\\benchmark\\user\\inner.html"));

        purseAndAddDetails(cpu,document);


        if (!isGetCpuScoreData(cpu)) {
            System.out.println(">>>>>>>>>>>>>!!!");
        } else {
            System.out.println(">>>>>>>>>>>>>OOOOOOOOOOOKKKKKKKKKKKKKKK");
        }

        parseCpuSpecificationTemplate2(cpu, document);
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

    private void parseCpuSpecificationTemplate2(UserBenchmarkCpu cpu, Document document) {
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
}
