package com.example.parser.service.parse.hotlinepageparser.impl;

import com.example.parser.model.hotline.MotherBoardHotLine;
import com.example.parser.service.parse.HotlinePageParser;
import com.example.parser.service.parse.HtmlDocumentFetcher;
import com.example.parser.utils.ParseUtil;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class HotlineMotherBoardPageParserImpl
        implements HotlinePageParser<MotherBoardHotLine> {
    private final HtmlDocumentFetcher htmlDocumentFetcher;
    private static final String DOMAIN_LINK = "https://hotline.ua";
    private static final String BASE_URL = "https://hotline.ua/ua/computer/materinskie-platy/?p=";
    private static final String TABLE_CSS_SELECTOR = "div.list-body__content.content.flex-wrap > div";
    private static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private static final ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE*2);

    @PostConstruct
    public void test() {
//        final List<MotherBoardHotLine> motherBoardHotLines = parsePage(BASE_URL + 18, true, true, 1, 3, false);
//        motherBoardHotLines.forEach(System.out::println);
//        parseAllPages();


        final List<MotherBoardHotLine> motherBoardHotLines = parseAllPagesMultiThread();
        motherBoardHotLines.forEach(System.out::println);
    }


    @Override
    public List<MotherBoardHotLine> parseAllPagesMultiThread() {
        int startPage = 1;
        int maxPage = findMaxPage();
        List<MotherBoardHotLine> parts = new ArrayList<>();
        List<Future<List<MotherBoardHotLine>>> futures = new ArrayList<>();


        for (int i = startPage; i <= maxPage; i++) {
            int pageIndex = i;
            Future<List<MotherBoardHotLine>> future = executor.submit(() -> {
                List<MotherBoardHotLine> parse = parsePage(
                        BASE_URL + pageIndex,
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

        for (Future<List<MotherBoardHotLine>> future : futures) {
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
    public List<MotherBoardHotLine> parseAllPages() {
        int startPage = 1;
        int maxPage = findMaxPage();
        List<MotherBoardHotLine> parts = new ArrayList<>();

        for (int i = startPage; i <= maxPage; i++) {
            int pageIndex = i;
            final List<MotherBoardHotLine> parse = parsePage(
                    BASE_URL + pageIndex,
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

    private List<MotherBoardHotLine> parsePage(
            String url,
            boolean useUserAgent,
            boolean useDelay,
            int delayFrom,
            int delayTo,
            boolean isPrintDocumentToConsole
    ) {
        Document htmlDocument = fetchHtmlDocumentWithRetries(url, useUserAgent, useDelay, delayFrom, delayTo, isPrintDocumentToConsole);
        return parseTableElements(htmlDocument);
    }

    private Document fetchHtmlDocumentWithRetries(
            String url,
            boolean useUserAgent,
            boolean useDelay,
            int delayFrom,
            int delayTo,
            boolean isPrintDocumentToConsole
    ) {
        int maxRetries = 5;
        int retryDelaySeconds = 5;
        int attempt = 0;
        Document htmlDocument = null;

        while (attempt < maxRetries) {
            attempt++;
            try {
                log.info("Attempting to fetch page: " + url + " (Attempt " + attempt + ")");
                htmlDocument = htmlDocumentFetcher.getHtmlDocumentFromWeb(
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
            sleepForRetry(++retryDelaySeconds);
        }

        log.error("Failed to load page after " + maxRetries + " attempts: " + url);
        throw new RuntimeException("Failed to load page: " + url);
    }

    private void sleepForRetry(int retryDelaySeconds) {
        try {
            TimeUnit.SECONDS.sleep(retryDelaySeconds);
        } catch (InterruptedException e) {
            log.error("Retry delay interrupted", e);
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted during retry delay", e);
        }
    }

    private List<MotherBoardHotLine> parseTableElements(Document htmlDocument) {
        Elements tableElements = htmlDocument.select(TABLE_CSS_SELECTOR);
        List<MotherBoardHotLine> motherBoards = new ArrayList<>();

        for (Element itemBlock : tableElements) {
            MotherBoardHotLine mb = new MotherBoardHotLine();
            setFields(mb, itemBlock);
            motherBoards.add(mb);
        }

        return motherBoards;
    }


//    private List<MotherBoardHotLine> parsePage(
//            String url,
//            boolean useUserAgent,
//            boolean useDelay,
//            int delayFrom,
//            int delayTo,
//            boolean isPrintDocumentToConsole
//    ) {
//        int maxRetries = 5; // Максимальное количество попыток
//        int retryDelaySeconds = 5; // Задержка между попытками
//        int attempt = 0;
//        Document htmlDocument = null;
//
//        while (attempt < maxRetries) {
//            attempt++;
//            try {
//                log.info("Attempting to fetch page: " + url + " (Attempt " + attempt + ")");
//                htmlDocument = htmlDocumentFetcher.getHtmlDocumentFromWeb(
//                        url,
//                        useUserAgent,
//                        useDelay,
//                        delayFrom,
//                        delayTo,
//                        isPrintDocumentToConsole);
//
//                if (htmlDocument != null && !htmlDocument.select(TABLE_CSS_SELECTOR).isEmpty()) {
//                    break;
//                } else {
//                    log.warn("Page " + url + " loaded but no data found. Retrying...");
//                }
//            } catch (Exception e) {
//                log.error("Error fetching page: " + url + " (Attempt " + attempt + ")", e);
//            }
//
//
//            try {
//                TimeUnit.SECONDS.sleep(retryDelaySeconds);
//            } catch (InterruptedException e) {
//                log.error("Retry delay interrupted", e);
//                Thread.currentThread().interrupt();
//                throw new RuntimeException("Interrupted during retry delay", e);
//            }
//        }
//
//        // Если после всех попыток документ не загрузился
//        if (htmlDocument == null || htmlDocument.select(TABLE_CSS_SELECTOR).isEmpty()) {
//            log.error("Failed to load page after " + maxRetries + " attempts: " + url);
//            throw new RuntimeException("Failed to load page: " + url);
//        }
//
//        Elements tableElements = htmlDocument.select(TABLE_CSS_SELECTOR);
//        List<MotherBoardHotLine> motherBoards = new ArrayList<>();
//
//        for (Element itemBlock : tableElements) {
//            MotherBoardHotLine mb = new MotherBoardHotLine();
//            setFields(mb, itemBlock);
//            motherBoards.add(mb);
//        }
//
//        return motherBoards;
//    }




//    private List<MotherBoardHotLine> parsePage(
//            String url,
//            boolean useUserAgent,
//            boolean useDelay,
//            int delayFrom,
//            int delayTo,
//            boolean isPrintDocumentToConsole
//    ) {
//        Document htmlDocument = htmlDocumentFetcher.getHtmlDocumentFromWeb(
//                url,
//                useUserAgent,
//                useDelay,
//                delayFrom,
//                delayTo,
//                isPrintDocumentToConsole);
//
//        Elements tableElements = htmlDocument.select(TABLE_CSS_SELECTOR);
//        List<MotherBoardHotLine> motherBoards = new ArrayList<>();
//
//        if (tableElements.isEmpty()) {
//            log.error("No data");
//            throw new RuntimeException();
//        } else {
//
//            for (Element itemBlock : tableElements) {
//                MotherBoardHotLine mb = new MotherBoardHotLine();
//                setFields(mb, itemBlock);
//                motherBoards.add(mb);
//                System.out.println(mb);
//            }
//
//        }
//
//        return motherBoards;
//    }

    private void setFields(MotherBoardHotLine mb, Element itemBlock) {
        mb.setUrl(DOMAIN_LINK + parseUrl(itemBlock));
        mb.setPropositionsQuantity(setPropositionQuantity(itemBlock, mb));
        parseAndSetManufacturerAndName(itemBlock, mb);
        String prices = parsePrices(itemBlock);
        mb.setPrices(prices);
        processTextToPriceAvg(prices, mb);
        Element characteristicsBlock = itemBlock.select("div.specs__text").first();
        parseDataFromCharacteristicsBlock(characteristicsBlock, mb);
    }


    private int setPropositionQuantity(Element itemBlock, MotherBoardHotLine mb) {

        final Elements noElement = itemBlock.select("div.list-item__value > div.list-item__value--overlay.list-item__value--full > div > div > div.m_b-10");
        if (!noElement.isEmpty()) {
            final String waitingText = noElement.text();

            if (waitingText.contains("Очікується в продажу")) {
                return 0;
            }
        }

        final Elements propositionQuantityElement = itemBlock.select("a.link.link--black.text-sm.m_b-5");
        if (!propositionQuantityElement.isEmpty()) {
            final String text = propositionQuantityElement.text().trim();
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(text);

            if (matcher.find()) {
                String number = matcher.group();
                return ParseUtil.stringToIntIfErrorReturnMinusOne(number);
            }
        }
        return 1;
    }

    private String parsePrices(Element itemBlock) {
        return itemBlock.select("div.list-item__value-price").text();
    }

    public void processTextToPriceAvg(String input, MotherBoardHotLine mb) {
        input = input.replace("грн", "").trim();
        String[] parts = input.split("–");
        double num1;
        if (parts.length == 2) {
            num1 = ParseUtil.stringToDoubleIfErrorReturnMinusOne(parts[0].trim().replace(" ", ""));
            double num2 = ParseUtil.stringToDoubleIfErrorReturnMinusOne(parts[1].trim().replace(" ", ""));
            mb.setAvgPrice((num1 + num2) / 2);
        } else if (parts.length == 1) {
            num1 = ParseUtil.stringToDoubleIfErrorReturnMinusOne(parts[0].trim().replace(" ", ""));
            mb.setAvgPrice(num1);
        } else {
            mb.setAvgPrice(0.00);
        }
    }

    private void parseAndSetManufacturerAndName(Element itemBlock, MotherBoardHotLine mb) {
        Element nameElement = itemBlock.select("div.list-item__title-container a").first();
        final String text = nameElement != null ? nameElement.text().trim() : "Не найдено";
        String manufacturer = text.split(" ")[0];
        String name = text.substring(manufacturer.length()).trim();
        mb.setManufacturer(manufacturer);
        mb.setName(name);
    }

    private String parseUrl(Element itemBlock) {
        Element linkElement = itemBlock.select("a.item-title.link--black").first();
        if (linkElement == null) {
            log.warn("Can't find url");
            return "";
        }
        return linkElement.attr("href");
    }

    private void parseDataFromCharacteristicsBlock(Element characteristicsBlock, MotherBoardHotLine mb) {
        if (characteristicsBlock == null) {
            log.warn("Can't find span element to parse data");
        } else {
            Elements positions = characteristicsBlock.select("span");

            if (false){
                //todo delete after
                iterateSpanCheckMode(positions);
            }

            for (Element position : positions) {
                extractData(position.text(), mb);
            }
        }
    }

    private void iterateSpanCheckMode(Elements positions) {
        for (int i = 0; i < positions.size(); i++) {
            System.out.println(i + " " + positions.get(i).text());
        }
    }

    private void extractData(String text, MotherBoardHotLine mb) {

        if (text.contains("Socket ")) {
            mb.setSocketType(
                    splitAndExtractDataByIndex(text, 1)
            );
        } else if (text.contains("Intel ")) {
            mb.setChipset(
                    splitAndExtractDataByIndex(text, 1)
            );
            mb.setChipsetManufacturer("Intel");
        } else if (text.contains("AMD ")) {
            mb.setChipset(
                    splitAndExtractDataByIndex(text, 1)
            );
            mb.setChipsetManufacturer("AMD");
        } else if (text.contains("DDR2")) {
            mb.setMemoryType(
                    "DDR2"
            );
        } else if (text.contains("DDR3")) {
            mb.setMemoryType(
                    "DDR3"
            );
        } else if (text.contains("DDR4")) {
            mb.setMemoryType(
                    "DDR4"
            );
        } else if (text.contains("DDR5")) {
            mb.setMemoryType(
                    "DDR5"
            );
        } else if (text.contains("DDR6")) {
            mb.setMemoryType(
                    "DDR6"
            );
        } else if (text.contains("ATX") || text.contains("ITX")) {
            mb.setCaseType(
                    splitAndExtractDataByIndex(text, 0).replaceAll(",+$", "")
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

    private int findMaxPage() {
        Document htmlDocument = htmlDocumentFetcher.getHtmlDocumentFromWeb(
                BASE_URL + 1,
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

    private void shutdownExecutor() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("Executor did not terminate");
                }
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

}
