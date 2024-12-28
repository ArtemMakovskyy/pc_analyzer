package com.example.parser.service.user.benchmark;

import com.example.parser.model.user.benchmark.CpuUserBenchmark;
import com.example.parser.service.HtmlDocumentFetcher;
import com.example.parser.utils.ParseUtil;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class CpuUserBenchmarkParser {
    private final CpuUserBenchmarkParserByPosition cpuUserBenchmarkParserByPosition;
    private final HtmlDocumentFetcher htmlDocumentFetcher;
    private static final String BASE_URL = "https://cpu.userbenchmark.com/";

    public List<Object> purseAllPages() {
        pursePage(BASE_URL);

        return Collections.emptyList();
    }

    public List<CpuUserBenchmark> pursePage(String url) {
        Document htmlDocument;
        List<CpuUserBenchmark> cpuUserBenchmarks = new ArrayList<>();

        boolean readFromWebPage = false;
        if (readFromWebPage) {
            htmlDocument = htmlDocumentFetcher.getHtmlDocumentAgent
                    (
                            url,
                            true
                            , true
                            , false
                    );

        } else {
            log.info("Read from file");
            // Read from local file
            File file = new File("C:\\Users\\Artem\\Documents\\Java\\UltimateJetBrains\\tutorials\\ms1\\parser\\parser\\src\\main\\resources\\static\\other\\UserBenchmark2.html");
            htmlDocument = null;
            try {
                htmlDocument = Jsoup.parse(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * <tr class="hovertarget " data-id="1943305">
         */
        Elements rows = htmlDocument.select("tr.hovertarget");
        CpuUserBenchmark cpu = null;

        for (Element row : rows) {
            String column1NumberPosition = row.select("td:nth-child(1) div")
                    .text();
            String column2_1Manufacturer = row.select("td:nth-child(2) span.semi-strongs")
                    .first().ownText().trim();
            String column2_2Model = row.select("td:nth-child(2) span.semi-strongs a.nodec")
                    .text().trim();
            String column3UserRating = row.select("td:nth-child(3) div.mh-tc")
                    .first().text().replaceAll("[^0-9]", "");
            String column4Value = row.select("td:nth-child(4) div.mh-tc")
                    .text();
            String column5_1Avg = row.select("td:nth-child(5) div.mh-tc")
                    .text().split(" ")[0];
            String column5_2AvgFromTo = row.select("td:nth-child(5) div.mh-tc-cap")
                    .text();
            String column6Memory = row.select("td:nth-child(6) div.mh-tc")
                    .text();
            String column7Core = row.select("td:nth-child(7) div.mh-tc")
                    .text();
            String column8Mkt = row.select("td:nth-child(8) div.mh-tc")
                    .text();
            String column9Age = row.select("td:nth-child(9) div.mh-tc")
                    .text();
            String column10Price = row.select("td:nth-child(10) div.mh-tc")
                    .text().replaceAll("[^0-9]", "");

//            if (false) {
//                System.out.println("Column 1: " + column1NumberPosition);
//                System.out.println("Column 201: " + column2_1Manufacturer);
//                System.out.println("Column 202: " + column2_2Model);
//                System.out.println("Column 3: " + column3UserRating);
//                System.out.println("Column 4: " + column4Value);
//                System.out.println("Column 5: " + column5_1Avg);
//                System.out.println("Column 5: " + column5_2AvgFromTo);
//                System.out.println("Column 6: " + column6Memory);
//                System.out.println("Column 7: " + column7Core);
//                System.out.println("Column 8: " + column8Mkt);
//                System.out.println("Column 9: " + column9Age);
//                System.out.println("Column 10: " + column10Price);
//                System.out.println("------------------------");
//            }

            cpu = new CpuUserBenchmark();
            cpu.setModel(column2_2Model);
            cpu.setManufacturer(column2_1Manufacturer);
            cpu.setUserRating(ParseUtil.stringToDouble(column3UserRating));
            cpu.setValuePercents(ParseUtil.stringToDouble(column4Value));
            cpu.setAvgBench(ParseUtil.stringToDouble(column5_1Avg));
            cpu.setMemoryPercents(ParseUtil.stringToDouble(column6Memory));
            cpu.setPrice(ParseUtil.stringToDouble(column10Price));
            cpu.setUrlOfCpu(row.select("td a.nodec").attr("href"));

            cpuUserBenchmarkParserByPosition.purseInnerPage(cpu);
            cpuUserBenchmarks.add(cpu);
        }

        return cpuUserBenchmarks;
    }

}

@Service
@Log4j2
class CpuUserBenchmarkParserByPosition {
    private static final String BASE_URL = "https://cpu.userbenchmark.com/Intel-Core-i5-13600K/Rating/4134";

    //    public void purseInnerPage(String url, CpuUserBenchmark cpu) {
    public void purseInnerPage(CpuUserBenchmark cpu) {
        boolean readFromWebPage = false;

        Document htmlDocument = null;
        if (readFromWebPage) {
//            HtmlDocumentFetcher.getInstance().getHtmlDocument(
//                    false, url);
        } else {
            // Read from local file
            File file = new File("C:\\Users\\Artem\\Documents\\Java\\UltimateJetBrains\\tutorials\\ms1\\parser\\parser\\src\\main\\resources\\static\\other\\Core i5-13600K.html");
            try {
                htmlDocument = Jsoup.parse(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        cpu.setPartNumber(htmlDocument.getElementsByClass("pg-head-toption-post").text());
        cpu.setCpuSpecification(htmlDocument.getElementsByClass("cmp-cpt tallp cmp-cpt-l").text());
        final Elements selects = htmlDocument.select("div.bsc-w.text-left.semi-strong > div");
        for (Element select : selects) {
            String text = select.text();
            if (text.contains("Desktop")) {
                cpu.setDesktopScore(ParseUtil.stringToDouble(text.replaceAll("[^0-9]", "")));
            } else if (text.contains("Gaming")) {
                cpu.setGamingScore(ParseUtil.stringToDouble(text.replaceAll("[^0-9]", "")));
            } else if (text.contains("Workstation")) {
                cpu.setWorkstationScore(ParseUtil.stringToDouble(text.replaceAll("[^0-9]", "")));
                break;
            }
        }
        System.out.println(cpu);
    }
}
