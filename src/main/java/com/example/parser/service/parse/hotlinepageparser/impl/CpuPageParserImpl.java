package com.example.parser.service.parse.hotlinepageparser.impl;

import com.example.parser.model.hotline.CpuHotLine;
import com.example.parser.service.parse.HtmlDocumentFetcher;
import com.example.parser.service.parse.MultiThreadPageParser;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class CpuPageParserImpl
        implements MultiThreadPageParser<CpuHotLine> {
    private final HtmlDocumentFetcher htmlDocumentFetcher;
    private static final String DOMAIN_LINK = "https://hotline.ua";
    private static final String BASE_URL = "https://hotline.ua/ua/computer/processory/?p=";
    private static final String TABLE_CSS_SELECTOR = "div.list-body__content.content.flex-wrap > div";
    private static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private static final ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);


    @Override
    public List<CpuHotLine> parseMultiThread() {
        int startPage = 1;
        int maxPage = findMaxPage(BASE_URL);
        List<CpuHotLine> parts = new ArrayList<>();
        List<Future<List<CpuHotLine>>> futures = new ArrayList<>();


        for (int i = startPage; i <= maxPage; i++) {
            int pageIndex = i;
            Future<List<CpuHotLine>> future = executor.submit(() -> {
                List<CpuHotLine> parse = parsePage(
                        BASE_URL + pageIndex,
                        true,
                        true,
                        8,
                        10,
                        false);
                log.info("... parsed page: " + pageIndex + " from: " + maxPage);
                return parse;
            });
            futures.add(future);
        }

        for (Future<List<CpuHotLine>> future : futures) {
            try {
                parts.addAll(future.get());
            } catch (InterruptedException | ExecutionException e) {
                log.error("Error parsing page", e);
                Thread.currentThread().interrupt();
            }
        }
        return parts;
    }

    @Override
    public List<CpuHotLine> parse() {
        int startPage = 1;
        int maxPage = findMaxPage(BASE_URL);
        List<CpuHotLine> parts = new ArrayList<>();

        for (int i = startPage; i <= maxPage; i++) {
            int pageIndex = i;
            final List<CpuHotLine> parse = parsePage(
                    BASE_URL + pageIndex,
                    true,
                    true,
                    2,
                    5,
                    false);
            parts.addAll(parse);
            log.info("... parsed page: " + i + " from: " + maxPage);
        }
        return parts;
    }

    private List<CpuHotLine> parsePage(
            String url,
            boolean useUserAgent,
            boolean useDelay,
            int delayFrom,
            int delayTo,
            boolean isPrintDocumentToConsole
    ) {
        Document htmlDocument = htmlDocumentFetcher.fetchDocument(
                url,
                useUserAgent,
                useDelay,
                delayFrom,
                delayTo,
                isPrintDocumentToConsole);

        Elements tableElements = htmlDocument.select(TABLE_CSS_SELECTOR);
        List<CpuHotLine> cpus = new ArrayList<>();

        if (tableElements.isEmpty()) {
            log.error("No data");
            throw new RuntimeException();
        } else {
            for (Element itemBlock : tableElements) {
                CpuHotLine cpu = new CpuHotLine();
                setFields(cpu, itemBlock);
                cpus.add(cpu);
            }
        }

        return cpus;
    }

    private void setFields(CpuHotLine cpu, Element itemBlock) {
        cpu.setUrl(DOMAIN_LINK + parseUrl(itemBlock));
        cpu.setName(parseName(itemBlock));
        cpu.setPrices(parsePrices(itemBlock));
        Element characteristicsBlock = itemBlock.select("div.specs__text").first();
        parseDataFromCharacteristicsBlock(characteristicsBlock, cpu);
    }

    private String parsePrices(Element cpuBlock) {
        return cpuBlock.select("div.list-item__value-price").text();
    }

    private String parseName(Element cpuBlock) {
        Element nameElement = cpuBlock.select("div.list-item__title-container a").first();
        return nameElement != null ? nameElement.text().trim() : "Не найдено";
    }

    private String parseUrl(Element cpuBlock) {
        Element linkElement = cpuBlock.select("a.item-title.link--black").first();
        if (linkElement == null) {
            log.warn("Can't find url");
            return "";
        }
        return linkElement.attr("href");
    }

    private void parseDataFromCharacteristicsBlock(Element characteristicsBlock, CpuHotLine cpu) {
        if (characteristicsBlock == null) {
            log.warn("Can't find span element to parse data");
        } else {
            Elements positions = characteristicsBlock.select("span");
            for (Element position : positions) {
                extractData(position.text(), cpu);
            }
        }
    }

    private void extractData(String text, CpuHotLine cpu) {

        if (text.contains("Роз'єм: Socket ")) {
            cpu.setSocketType(
                    splitAndExtractDataByIndex(text, 2)
            );
        } else if (text.contains("Частота: ")) {
            cpu.setFrequency(
                    splitAndExtractDataByIndex(text, 1)
            );
        } else if (text.contains("Кеш третього рівня: ")) {
            cpu.setL3Cache(
                    splitAndExtractDataByIndex(text, 3)
            );
        } else if (text.contains("Кількість ядер: ")) {
            cpu.setCores(
                    splitAndExtractDataByIndex(text, 2)
            );
        } else if (text.contains("Число потоків: ")) {
            cpu.setThreads(
                    splitAndExtractDataByIndex(text, 2)
            );
        } else if (text.contains("Комплектація: Tray")) {
            cpu.setPackageType(
                    "Tray"
            );
        } else if (text.contains("Комплектація: Box")) {
            cpu.setPackageType(
                    "Box"
            );
        } else {
            // ignore
        }

    }

    private String splitAndExtractDataByIndex(String text, int index) {
        String[] textArray = text.split(" ");
        if (textArray.length < index) {
            log.warn("HotlineCpuPageParser.splitAndExtractData(): Invalid index "
                    + index + ". Text array length is " + textArray.length);
            return "";
        }
        return textArray[index];
    }

    private int findMaxPage(String baseUrl) {
        Document htmlDocument = htmlDocumentFetcher.fetchDocument(
                baseUrl + 1,
                true,
                false,
                false);

        Elements pages = htmlDocument.select("a.page");
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


}
