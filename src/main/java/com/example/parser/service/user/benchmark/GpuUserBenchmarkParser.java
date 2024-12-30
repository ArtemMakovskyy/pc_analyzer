package com.example.parser.service.user.benchmark;

import com.example.parser.model.user.benchmark.CpuUserBenchmark;
import com.example.parser.model.user.benchmark.GpuUserBenchmark;
import com.example.parser.service.HtmlDocumentFetcher;
import com.example.parser.utils.ParseUtil;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
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
public class GpuUserBenchmarkParser {
    private final HtmlDocumentFetcher htmlDocumentFetcher;
    private static final String BASE_URL = "https://gpu.userbenchmark.com/";

    private static final String BASE_LINK = "C:\\Users\\Artem\\Documents\\Java"
            + "\\UltimateJetBrains\\tutorials\\ms1\\parser\\parser\\src\\main\\resources\\static"
            + "\\other\\userbenchmark\\gpu\\";

    public List<GpuUserBenchmark> purseAllPages() {
        List<GpuUserBenchmark> gpuUserBenchmarks = new ArrayList<>();
        if (false){
            for (int i = 1; i <= 4; i++) {
                gpuUserBenchmarks.addAll(pursePageFromFile(BASE_LINK + "ub_gpu_0" + i + ".html"));
            }
            return gpuUserBenchmarks;
        }else {
            List<CompletableFuture<List<GpuUserBenchmark>>> futures = new ArrayList<>();
            for (int i = 1; i <= 4; i++) {
                String filePath = BASE_LINK + "ub_gpu_0" + i + ".html";
                futures.add(CompletableFuture.supplyAsync(() -> pursePageFromFile(filePath)));
            }
             gpuUserBenchmarks = futures.stream()
                    .map(CompletableFuture::join)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
        }
        return gpuUserBenchmarks;
    }

    private List<GpuUserBenchmark> pursePageFromFile(String link) {
        Document htmlDocument
                = htmlDocumentFetcher.getHtmlDocumentFromFile(
                link, false);
        return processDocument(htmlDocument);
    }


    public List<GpuUserBenchmark> pursePageFromUrl(String url) {
        Document htmlDocument;
        List<GpuUserBenchmark> items = new ArrayList<>();

        boolean readFromWebPage = false;
        if (readFromWebPage) {
            htmlDocument = htmlDocumentFetcher.getHtmlDocumentFromWeb
                    (
                            url,
                            true
                            , true
                            , true
                    );

        } else {
            log.info("Read from file");
            // Read from local file
            File file = new File("C:\\Users\\Artem\\Documents\\Java\\UltimateJetBrains\\tutorials\\ms1\\parser\\parser\\src\\main\\resources\\static\\other\\userbenchmark\\gpu\\ub_gpu_01.html");
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
        GpuUserBenchmark item ;
        for (Element row : rows) {
            item = rowToGpu(row);
            items.add(item);
        }
        return items;
    }

    private List<GpuUserBenchmark> processDocument(Document htmlDocument) {

        List<GpuUserBenchmark> gpuUserBenchmarks = new ArrayList<>();

        /**
         * <tr class="hovertarget " data-id="1943305">
         */
        Elements rows = htmlDocument.select("tr.hovertarget");
        GpuUserBenchmark gpu;
        for (Element row : rows) {
            gpu = rowToGpu(row);
            gpuUserBenchmarks.add(gpu);
        }
        return gpuUserBenchmarks;
    }

    private GpuUserBenchmark rowToGpu(Element row) {
        //            String column1NumberPosition = row.select("td:nth-child(1) div")
//                    .text();
        String column2_1Manufacturer = row.select("td:nth-child(2) span.semi-strongs")
                .first().ownText().trim();
        String column2_2Model = row.select("td:nth-child(2) span.semi-strongs a.nodec")
                .text().trim();
        String column3UserRating = row.select("td:nth-child(3) div.mh-tc")
                .first().text().replaceAll("[^0-9]", "");
        String column4Value = row.select("td:nth-child(4) div.mh-tc")
                .text().trim();
        String column5_1Avg = row.select("td:nth-child(5) div.mh-tc")
                .text().split(" ")[0];
//            String column5_2AvgFromTo = row.select("td:nth-child(5) div.mh-tc-cap")
//                    .text();
        String column6Memory = row.select("td:nth-child(6) div.mh-tc")
                .text();
            String column7Core = row.select("td:nth-child(7) div.mh-tc")
                    .text();
            String column8Price = row.select("td:nth-child(8) div.mh-tc")
                    .text().replaceAll("[^0-9]", "");
            String column9Age = row.select("td:nth-child(9) div.mh-tc")
                    .text();
        String column10Price = row.select("td:nth-child(10) div.mh-tc")
                .text().replaceAll("[^0-9]", "");

            if (false) {
//                System.out.println("Column 1: " + column1NumberPosition);
                System.out.println("Column 201: " + column2_1Manufacturer);
                System.out.println("Column 202: " + column2_2Model);
                System.out.println("Column 3: " + column3UserRating);
                System.out.println("Column 4: " + column4Value);
                System.out.println("Column 5: " + column5_1Avg);
//                System.out.println("Column 5: " + column5_2AvgFromTo);
                System.out.println("Column 6: " + column6Memory);
                System.out.println("Column 7: " + column7Core);
                System.out.println("Column 8: " + column8Price);
                System.out.println("Column 9: " + column9Age);
                System.out.println("Column 10: " + column10Price);
                System.out.println("------------------------");
            }
        GpuUserBenchmark item = new GpuUserBenchmark();
        item.setModel(column2_2Model);
        item.setManufacturer(column2_1Manufacturer);
        item.setUserRating(ParseUtil.stringToDouble(column3UserRating));
        item.setValuePercents(ParseUtil.stringToDouble(column4Value));
        item.setAvgBench(ParseUtil.stringToDouble(column5_1Avg));
        item.setPrice(ParseUtil.stringToDouble(column8Price));
        item.setUrlOfCpu(row.select("td a.nodec").attr("href"));
        return item;
    }
}
