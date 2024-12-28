package com.example.parser.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public final class HtmlDocumentFetcher {

    private static final Logger log = LoggerFactory.getLogger(HtmlDocumentFetcher.class);
    private static HtmlDocumentFetcher instance;


    private HtmlDocumentFetcher() {
    }

    public static synchronized HtmlDocumentFetcher getInstance() {
        if (instance == null) {
            instance = new HtmlDocumentFetcher();
        }
        return instance;
    }

    public Document getHtmlDocumentAgent(boolean isPrintDocumentToConsole, String url) {
        final Connection connect = Jsoup.connect(url);
        Document document = null;
        try {
            document = connect.get();

            if (isPrintDocumentToConsole) {
                log.info(document.toString());
            }
            log.info("Connected to the page: " + url);
        } catch (IOException e) {
            log.error("Can't get page: " + url, e);
            throw new RuntimeException(e);
        }
        return document;
    }

    public Document getHtmlDocumentAgent(
            String url,
            boolean useUserAgent,
            boolean useDelay,
            boolean isPrintDocumentToConsole) {

        Document document = null;
        Connection connect = null;

        try {
            addRandomDelayInSeconds(1, 7, useDelay);
            connect = Jsoup.connect(url);

            if (useUserAgent) {
                connect.userAgent(getUserAgents().get("Chrome_Windows"));

            }

            if (true){
                connect.header("Accept-Language", "en-US,en;q=0.9")
                        .header("Accept-Encoding", "gzip, deflate, br")
                        .header("Connection", "keep-alive")
                        .header("Upgrade-Insecure-Requests", "1");
            }

            document = connect.get();

        } catch (IOException e) {
            log.error("Can't get page: " + url, e);
            throw new RuntimeException(e);
        }

        if (isPrintDocumentToConsole) {
            log.info(document.toString());
        }

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
