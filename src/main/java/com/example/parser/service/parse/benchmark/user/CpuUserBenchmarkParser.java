package com.example.parser.service.parse.benchmark.user;

import com.example.parser.model.user.benchmark.CpuUserBenchmark;

import com.example.parser.service.parse.HtmlDocumentFetcher;
import com.example.parser.utils.ParseUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
    private static final String BASE_LINK = "C:\\Users\\Artem\\Documents\\Java"
            + "\\UltimateJetBrains\\tutorials\\ms1\\parser\\parser\\src\\main\\resources\\static"
            + "\\other\\userbenchmark\\cpu\\";

    public List<CpuUserBenchmark> purseAllPages() {
        List<CpuUserBenchmark> cpuUserBenchmarks = new ArrayList<>();
        for (int i = 8; i <= 8; i++) {
            cpuUserBenchmarks.addAll(pursePageFromFile(BASE_LINK + "ub_cpu_0" + i + ".html"));
        }
        return cpuUserBenchmarks;
    }

    private List<CpuUserBenchmark> pursePageFromFile(String link) {
        Document htmlDocument
                = htmlDocumentFetcher.getHtmlDocumentFromFile(
                link, false);
        return processDocument(htmlDocument);
    }

    private List<CpuUserBenchmark> pursePageFromUrl(String link) {
        Document htmlDocument = htmlDocumentFetcher.getHtmlDocumentFromWeb
                (
                        link,
                        true
                        , true
                        , false
                );

        List<CpuUserBenchmark> cpuUserBenchmarks = processDocument(htmlDocument);
        return cpuUserBenchmarks;
    }

    private List<CpuUserBenchmark> processDocument(Document htmlDocument) {

        List<CpuUserBenchmark> cpuUserBenchmarks = new ArrayList<>();

        /**
         * <tr class="hovertarget " data-id="1943305">
         */
        Elements rows = htmlDocument.select("tr.hovertarget");
        CpuUserBenchmark cpu;
        for (Element row : rows) {
            cpu = rowToCpu(row);
//            cpuUserBenchmarkParserByPosition.purseInnerPage(cpu);
            cpuUserBenchmarks.add(cpu);
        }
        return cpuUserBenchmarks;
    }


    private CpuUserBenchmark rowToCpu(Element row) {

        //            String column1NumberPosition = row.select("td:nth-child(1) div")
//                    .text();
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
//            String column5_2AvgFromTo = row.select("td:nth-child(5) div.mh-tc-cap")
//                    .text();
        String column6Memory = row.select("td:nth-child(6) div.mh-tc")
                .text();
//            String column7Core = row.select("td:nth-child(7) div.mh-tc")
//                    .text();
//            String column8Mkt = row.select("td:nth-child(8) div.mh-tc")
//                    .text();
//            String column9Age = row.select("td:nth-child(9) div.mh-tc")
//                    .text();
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
        CpuUserBenchmark cpu = new CpuUserBenchmark();
        cpu.setModel(column2_2Model);
        cpu.setManufacturer(column2_1Manufacturer);
        cpu.setUserRating(ParseUtil.stringToDouble(column3UserRating));
        cpu.setValuePercents(ParseUtil.stringToDouble(column4Value));
        cpu.setAvgBench(ParseUtil.stringToDouble(column5_1Avg));
        cpu.setMemoryPercents(ParseUtil.stringToDouble(column6Memory));
        cpu.setPrice(ParseUtil.stringToDouble(column10Price));
        cpu.setUrlOfCpu(row.select("td a.nodec").attr("href"));
        return cpu;
    }
}
