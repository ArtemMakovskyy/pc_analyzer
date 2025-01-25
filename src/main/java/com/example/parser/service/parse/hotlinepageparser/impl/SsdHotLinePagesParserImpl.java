package com.example.parser.service.parse.hotlinepageparser.impl;

import com.example.parser.model.hotline.SsdHotLine;
import com.example.parser.model.hotline.SsdHotLine;
import com.example.parser.service.parse.HtmlDocumentFetcher;
import com.example.parser.service.parse.utils.ParseUtil;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class SsdHotLinePagesParserImpl extends HotLinePagesParserAbstract<SsdHotLine> {
    private static final String BASE_URL = "https://hotline.ua/ua/computer/materinskie-platy/?p=";
    private static final String CHARACTERISTICS_BLOCK_CSS_SELECTOR = "div.specs__text";
    private static final String PRICE_CSS_SELECTOR = "div.list-item__value-price";
    private static final String NAME_CSS_SELECTOR = "div.list-item__title-container a";
    private static final String LINK_CSS_SELECTOR = "a.item-title.link--black";
    private static final String PROPOSITION_QUANTITY_CSS_SELECTOR
            = "a.link.link--black.text-sm.m_b-5";
    private static final String NO_ELEMENT_CSS_SELECTOR
            = "div.list-item__value > div.list-item__value--overlay."
            + "list-item__value--full > div > div > div.m_b-10";
    private static final String DIGITS_REGEX = "\\d+";

    public SsdHotLinePagesParserImpl(HtmlDocumentFetcher htmlDocumentFetcher) {
        super(htmlDocumentFetcher, BASE_URL);
    }

//    @PostConstruct
//    public void init() {
//        final List<SsdHotLine> SsdHotLines = parsePage("https://hotline.ua/ua/computer/diski-ssd/66705-301666-301678-301686-608078-667303/");
//        SsdHotLines.forEach(System.out::println);
//    }

    @Override
    protected List<SsdHotLine> parseData(Document htmlDocument) {
        Elements tableElements = htmlDocument.select(TABLE_CSS_SELECTOR);
        List<SsdHotLine> motherBoards = new ArrayList<>();

        for (Element itememorylock : tableElements) {
            SsdHotLine memory = new SsdHotLine();
            setFields(memory, itememorylock);
            motherBoards.add(memory);
        }

        return motherBoards;
    }

    private void setFields(SsdHotLine memory, Element itememorylock) {
        memory.setUrl(DOMAIN_LINK + parseUrl(itememorylock));
        memory.setPropositionsQuantity(setPropositionQuantity(itememorylock));
        parseAndSetManufacturerAndName(itememorylock, memory);
        String prices = parsePrices(itememorylock);
        memory.setPrices(prices);
        processTextToPriceAvg(prices, memory);
        Element characteristicsBlock
                = itememorylock.select(CHARACTERISTICS_BLOCK_CSS_SELECTOR).first();
        parseDataFromCharacteristicsBlock(characteristicsBlock, memory);
    }

    private int setPropositionQuantity(Element itememorylock) {
        Elements noElement = itememorylock.select(NO_ELEMENT_CSS_SELECTOR);
        if (!noElement.isEmpty()) {
            String waitingText = noElement.text();

            if (waitingText.contains("Очікується в продажу")) {
                return 0;
            }
        }

        Elements propositionQuantityElement = itememorylock.select(PROPOSITION_QUANTITY_CSS_SELECTOR);
        if (!propositionQuantityElement.isEmpty()) {
            String text = propositionQuantityElement.text().trim();
            Pattern pattern = Pattern.compile(DIGITS_REGEX);
            Matcher matcher = pattern.matcher(text);

            if (matcher.find()) {
                String numemoryer = matcher.group();
                return ParseUtil.stringToIntIfErrorReturnMinusOne(numemoryer);
            }
        }
        return 1;
    }

    private String parsePrices(Element itememorylock) {
        return itememorylock.select(PRICE_CSS_SELECTOR).text();
    }

    public void processTextToPriceAvg(String input, SsdHotLine memory) {
        input = input.replace("грн", "").trim();
        String[] parts = input.split("–");
        double num1;
        if (parts.length == 2) {
            num1 = ParseUtil.stringToDoubleIfErrorReturnMinusOne(
                    parts[0].trim().replace(" ", ""));
            double num2 = ParseUtil.stringToDoubleIfErrorReturnMinusOne(
                    parts[1].trim().replace(" ", ""));
            memory.setAvgPrice((num1 + num2) / 2);
        } else if (parts.length == 1) {
            num1 = ParseUtil.stringToDoubleIfErrorReturnMinusOne(
                    parts[0].trim().replace(" ", ""));
            memory.setAvgPrice(num1);
        } else {
            memory.setAvgPrice(0.00);
        }
    }

    private void parseAndSetManufacturerAndName(Element itememorylock, SsdHotLine memory) {
        Element nameElement = itememorylock.select(NAME_CSS_SELECTOR).first();
        String text = nameElement != null ? nameElement.text().trim() : "Не найдено";
        String manufacturer = text.split(" ")[0];
        String name = text.substring(manufacturer.length()).trim();
        memory.setManufacturer(manufacturer);
        memory.setName(name);
    }

    private String parseUrl(Element itememorylock) {
        Element linkElement = itememorylock.select(LINK_CSS_SELECTOR).first();
        if (linkElement == null) {
            log.warn("Can't find url");
            return "";
        }
        return linkElement.attr("href");
    }

    private void parseDataFromCharacteristicsBlock(
            Element characteristicsBlock, SsdHotLine memory) {
        if (characteristicsBlock == null) {
            log.warn("Can't find span element to parse data");
        } else {
            Elements positions = characteristicsBlock.select("span");

            boolean isCheckMode = false;
            if (isCheckMode) {
                iterateSpanCheckMode(positions);
            }

            for (Element position : positions) {
                extractData(position.text(), memory);
            }
        }
    }

    private void iterateSpanCheckMode(Elements positions) {
        for (int i = 0; i < positions.size(); i++) {
            System.out.println(i + " " + positions.get(i).text());
        }
    }

    private void extractData(String text, SsdHotLine memory) {
        if (text.contains(" ГБ")) {
            memory.setCapacity(
                    splitAndExtractDataByIndex(text, 0)
            );
        } else if (text.contains("SSD ")) {
            memory.setType(
                    "SSD"
            );
        } else if (text.contains("Швидкість читання: ")) {
            memory.setReadingSpeed(
                   text.replace("Швидкість читання: ","")
            );
        } else if (text.contains("Швидкість запису: ")) {
            memory.setWritingSpeed(
                    text.replace("Швидкість запису: ","")
            );
        }

    }

    private String splitAndExtractDataByIndex(String text, int index) {
        String[] textArray = text.split(" ");
        if (textArray.length < index) {
            log.warn(this.getClass() + ": Invalid index "
                    + index + ". Text array length is " + textArray.length);
            return "";
        }
        return textArray[index];
    }

}
