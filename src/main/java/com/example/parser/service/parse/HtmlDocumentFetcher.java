package com.example.parser.service.parse;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class HtmlDocumentFetcher {

    public Document getHtmlDocumentFromFile(String link, boolean isPrintDocumentToConsole) {
        log.info("Reading from file: {}", link);
        File file = new File(link);

        if (!file.exists()) {
            log.error("File not found: {}", link);
            throw new IllegalArgumentException("File not found: " + link);
        }

        Document htmlDocument;
        try {
            htmlDocument = Jsoup.parse(file, "UTF-8");
        } catch (IOException e) {
            log.error("Error while parsing the file: {}", link, e);
            throw new RuntimeException("Error reading HTML document from file", e);
        }

        if (isPrintDocumentToConsole) {
            log.info(htmlDocument);
        }

        log.info("Document successfully parsed.");
        return htmlDocument;
    }

    public Document getHtmlDocumentFromWeb(
            String url,
            boolean useUserAgent,
            boolean useDelay,
            boolean isPrintDocumentToConsole) {
        return getHtmlDocumentFromWeb(
                url,
                useUserAgent,
                useDelay,
                2,
                5,
                isPrintDocumentToConsole
        );
    }

    public Document getHtmlDocumentFromWeb(
            String url,
            boolean useUserAgent,
            boolean useDelay,
            int delayFrom,
            int delayTo,
            boolean isPrintDocumentToConsole) {

        Connection connect = null;

        Document document;
        try {
            addRandomDelayInSeconds(delayFrom, delayTo, useDelay);

            if (useUserAgent) {
                connect = Jsoup.connect(url)
                        .maxBodySize(0)
                        .timeout(60 * 1000)
                        .userAgent(getUserAgents().get("Chrome_Windows"))
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true);
//                        .cookie()
//                        .execute();

            } else {
                connect = Jsoup.connect(url);
            }

            if (false) {
                //  header don't work
                connect.header("Accept-Language", "en-US,en;q=0.9")
                        .header("Accept-Encoding", "gzip, deflate, br")
                        .header("Connection", "keep-alive")
                        .header("Upgrade-Insecure-Requests", "1");
            }
            document = connect.get();
//        } catch (InterruptedException e) {
//            // Обработка InterruptedException
//            log.error("Поток был прерван во время выполнения", e);
//            Thread.currentThread().interrupt(); // Важно восстановить статус прерывания
//            throw new RuntimeException("Операция была прервана", e);
        } catch (IOException e) {
            log.error("Can't get page: " + url, e);
            throw new RuntimeException(e);
        }

        if (isPrintDocumentToConsole) {
            log.info(document.toString());
        }
        log.info("Connected to the page: " + url);
        return document;
    }

    private void addRandomDelayInSeconds(int fromSec, int toSec, boolean isDelay) {
        if (isDelay) {
            if (fromSec < 0 || toSec < 0 || fromSec > toSec) {
                log.warn("Invalid delay range specified. Ensure 0 <= fromSec <= toSec.");
                return;
            }

            if (fromSec == 0 && toSec == 0) {
                log.info("No delay as both fromSec and toSec are zero.");
                return;
            }

            Random random = new Random();
            int delay = fromSec + random.nextInt(toSec - fromSec + 1);

            try {
                log.info("Delay for " + delay + " seconds.");
                Thread.sleep(delay * 1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Thread was interrupted during sleep", e);
            }
        }
    }

    private Map<String, String> getUserAgents() {
        Map<String, String> userAgentMap = new HashMap<>();
        userAgentMap.put("Chrome_Windows", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36");
        userAgentMap.put("Firefox_Windows", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:102.0) Gecko/20100101 Firefox/102.0");
        userAgentMap.put("Edge_Windows", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36 Edg/107.0.0.0");
        userAgentMap.put("Safari_macOS", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.1 Safari/605.1.15");
        userAgentMap.put("Chrome_Android", "Mozilla/5.0 (Linux; Android 10; SM-G970F) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Mobile Safari/537.36");
        userAgentMap.put("Firefox_Android", "Mozilla/5.0 (Android 10; Mobile; rv:102.0) Gecko/102.0 Firefox/102.0");
        userAgentMap.put("Safari_iOS", "Mozilla/5.0 (iPhone; CPU iPhone OS 14_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0 Mobile/15E148 Safari/604.1");
        userAgentMap.put("Chrome_macOS", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36");
        userAgentMap.put("Chrome_iOS", "Mozilla/5.0 (iPhone; CPU iPhone OS 14_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/107.0.0.0 Mobile/15E148 Safari/604.1");
        userAgentMap.put("Firefox_macOS", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7; rv:102.0) Gecko/20100101 Firefox/102.0");
        return userAgentMap;
    }

    private Map<String, String> getDefaultHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept-Language", "en-US,en;q=0.9");
        headers.put("Accept-Encoding", "gzip, deflate, br");
        headers.put("Connection", "keep-alive");
        headers.put("Upgrade-Insecure-Requests", "1");
        return headers;
    }
}
