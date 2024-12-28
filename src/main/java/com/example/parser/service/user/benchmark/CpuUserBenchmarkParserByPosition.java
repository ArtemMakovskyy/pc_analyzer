package com.example.parser.service.user.benchmark;

import com.example.parser.model.user.benchmark.CpuUserBenchmark;
import com.example.parser.service.HtmlDocumentFetcher;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class CpuUserBenchmarkParserByPosition {
    private static final String BASE_URL = "https://cpu.userbenchmark.com/Intel-Core-i5-13600K/Rating/4134";


//    public void purseInnerPage(String url, CpuUserBenchmark cpu) {
    public void purseInnerPage( CpuUserBenchmark cpu) {






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
                cpu.setDesktopScore(toDouble(text.replaceAll("[^0-9]", "")));
            } else if (text.contains("Gaming")) {
                cpu.setGamingScore(toDouble(text.replaceAll("[^0-9]", "")));
            } else if (text.contains("Workstation")) {
                cpu.setWorkstationScore(toDouble(text.replaceAll("[^0-9]", "")));
                break;
            }
        }
        System.out.println(cpu);
    }

    private Double toDouble(String priceText) {
        double price = 0.0;
        try {
            // Check if line not empty and consists from digits
            if (!priceText.isEmpty() && priceText.matches("\\d+")) {
                price = Double.parseDouble(priceText);
            }
        } catch (NumberFormatException e) {
            price = 0.0;
        }
        return price;
    }
}
