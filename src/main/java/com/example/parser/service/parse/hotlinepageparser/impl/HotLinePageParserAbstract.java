package com.example.parser.service.parse.hotlinepageparser.impl;

import com.example.parser.service.parse.HtmlDocumentFetcher;
import com.example.parser.service.parse.MultiThreadPageParser;
import com.example.parser.service.parse.utils.ParseUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Log4j2
public abstract class HotLinePageParserAbstract<T> implements MultiThreadPageParser<T> {
    protected static final String DOMAIN_LINK = "https://hotline.ua";
    protected static final String PAGES_CSS_SELECTOR = "a.page";
    protected static final String TABLE_CSS_SELECTOR = "div.list-body__content.content.flex-wrap > div";
    protected static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    protected static final ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    protected final HtmlDocumentFetcher htmlDocumentFetcher;
    protected final String baseUrl;

    public HotLinePageParserAbstract(HtmlDocumentFetcher htmlDocumentFetcher, String baseUrl) {
        this.htmlDocumentFetcher = htmlDocumentFetcher;
        this.baseUrl = baseUrl;
    }

    @Override
    public List<T> parseMultiThread() {
        int startPage = 1;
        int maxPage = findMaxPage(baseUrl + 1);
        List<T> parts = new ArrayList<>();
        List<Future<List<T>>> futures = new ArrayList<>();

        for (int i = startPage; i <= maxPage; i++) {
            int pageIndex = i;
            Future<List<T>> future = executor.submit(() -> {
                List<T> parse = parsePage(
                        baseUrl + pageIndex,
                        true,
                        true,
                        4,
                        8,
                        false);
                log.info("... parsed page: " + pageIndex + " from: " + maxPage);
                return parse;
            });
            futures.add(future);
        }

        for (Future<List<T>> future : futures) {
            try {
                parts.addAll(future.get());
            } catch (InterruptedException | ExecutionException e) {
                log.error("Error parsing page", e);
                Thread.currentThread().interrupt();
            }
        }
        shutdownExecutor();
        return parts;
    }

    @Override
    public List<T> parse() {
        int startPage = 1;
        int maxPage = findMaxPage(baseUrl + 1);
        List<T> parts = new ArrayList<>();

        for (int i = startPage; i <= maxPage; i++) {
            int pageIndex = i;
            final List<T> parse = parsePage(
                    baseUrl + pageIndex,
                    true,
                    true,
                    2,
                    4,
                    false);
            parts.addAll(parse);
            log.info("... parsed page: " + i + " from: " + maxPage);
        }
        return parts;
    }

    protected List<T> parsePage(
            String url,
            boolean useUserAgent,
            boolean useDelay,
            int delayFrom,
            int delayTo,
            boolean isPrintDocumentToConsole
    ) {
        Document htmlDocument = fetchHtmlDocumentWithRetries(url, useUserAgent, useDelay, delayFrom, delayTo, isPrintDocumentToConsole);
        return parseData(htmlDocument);
    }

    protected Document fetchHtmlDocumentWithRetries(
            String url,
            boolean useUserAgent,
            boolean useDelay,
            int delayFrom,
            int delayTo,
            boolean isPrintDocumentToConsole
    ) {
        int maxRetries = 5;
        int attempt = 0;
        Document htmlDocument = null;

        while (attempt < maxRetries) {
            attempt++;
            try {
                log.info("Attempting to fetch page: " + url + " (Attempt " + attempt + ")");
                htmlDocument = htmlDocumentFetcher.fetchDocument(
                        url,
                        useUserAgent,
                        useDelay,
                        delayFrom,
                        delayTo,
                        isPrintDocumentToConsole);

                if (htmlDocument != null && !htmlDocument.select(TABLE_CSS_SELECTOR).isEmpty()) {
                    return htmlDocument;
                } else {
                    log.warn("Page " + url + " loaded but no data found. Retrying...");
                }
            } catch (Exception e) {
                log.error("Error fetching page: " + url + " (Attempt " + attempt + ")", e);
            }
            //sleepForRetry
            ParseUtil.applyRandomDelay(3, 6, useDelay);
        }

        log.error("Failed to load page after " + maxRetries + " attempts: " + url);
        throw new RuntimeException("Failed to load page: " + url);
    }

    protected abstract List<T> parseData(Document htmlDocument);

    protected int findMaxPage(String baseUrl) {
        Document htmlDocument = htmlDocumentFetcher.fetchDocument(
                baseUrl,
                true,
                false,
                false);

        Elements pages = htmlDocument.select(PAGES_CSS_SELECTOR);
        int maxPage = 0;

        for (Element page : pages) {
            String pageText = page.text().trim();
            try {
                int pageNumber = Integer.parseInt(pageText);
                maxPage = Math.max(maxPage, pageNumber);
            } catch (NumberFormatException e) {
                log.info("Cant get number with pageText: " + pageText + " Ignore it.");
            }
        }
        log.info("Pages quality: " + maxPage);
        return maxPage;
    }

    protected void shutdownExecutor() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    log.error("Executor did not terminate");
                }
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

}
