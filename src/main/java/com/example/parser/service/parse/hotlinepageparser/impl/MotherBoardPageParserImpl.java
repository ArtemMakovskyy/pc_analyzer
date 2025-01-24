package com.example.parser.service.parse.hotlinepageparser.impl;

import com.example.parser.model.hotline.MotherBoardHotLine;
import com.example.parser.service.parse.HtmlDocumentFetcher;
import com.example.parser.service.parse.utils.ParseUtil;
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
public class MotherBoardPageParserImpl extends HotLinePageParserAbstract<MotherBoardHotLine> {
    private static final String BASE_URL = "https://hotline.ua/ua/computer/materinskie-platy/?p=";
    private static final String CHARACTERISTICS_BLOCK_CSS_SELECTOR = "div.specs__text";
    private static final String PRICE_CSS_SELECTOR = "div.list-item__value-price";
    private static final String NAME_CSS_SELECTOR = "div.list-item__title-container a";
    private static final String PROPOSITION_QUANTITY_CSS_SELECTOR = "a.link.link--black.text-sm.m_b-5";
    private static final String LINK_CSS_SELECTOR = "a.item-title.link--black";
    private static final String NO_ELEMENT_CSS_SELECTOR
            = "div.list-item__value > div.list-item__value--overlay."
            + "list-item__value--full > div > div > div.m_b-10";
    private static final String DIGITS_REGEX = "\\d+";

    public MotherBoardPageParserImpl(HtmlDocumentFetcher htmlDocumentFetcher) {
        super(htmlDocumentFetcher, BASE_URL);
    }

    @Override
    protected List<MotherBoardHotLine> parseTableElements(Document htmlDocument) {
        Elements tableElements = htmlDocument.select(TABLE_CSS_SELECTOR);
        List<MotherBoardHotLine> motherBoards = new ArrayList<>();

        for (Element itemBlock : tableElements) {
            MotherBoardHotLine mb = new MotherBoardHotLine();
            setFields(mb, itemBlock);
            motherBoards.add(mb);
        }

        return motherBoards;
    }

    private void setFields(MotherBoardHotLine mb, Element itemBlock) {
        mb.setUrl(DOMAIN_LINK + parseUrl(itemBlock));
        mb.setPropositionsQuantity(setPropositionQuantity(itemBlock, mb));
        parseAndSetManufacturerAndName(itemBlock, mb);
        String prices = parsePrices(itemBlock);
        mb.setPrices(prices);
        processTextToPriceAvg(prices, mb);
        Element characteristicsBlock
                = itemBlock.select(CHARACTERISTICS_BLOCK_CSS_SELECTOR).first();
        parseDataFromCharacteristicsBlock(characteristicsBlock, mb);
    }


    private int setPropositionQuantity(Element itemBlock, MotherBoardHotLine mb) {
        Elements noElement = itemBlock.select(NO_ELEMENT_CSS_SELECTOR);
        if (!noElement.isEmpty()) {
            String waitingText = noElement.text();

            if (waitingText.contains("Очікується в продажу")) {
                return 0;
            }
        }

        Elements propositionQuantityElement = itemBlock.select(PROPOSITION_QUANTITY_CSS_SELECTOR);
        if (!propositionQuantityElement.isEmpty()) {
            String text = propositionQuantityElement.text().trim();
            Pattern pattern = Pattern.compile(DIGITS_REGEX);
            Matcher matcher = pattern.matcher(text);

            if (matcher.find()) {
                String number = matcher.group();
                return ParseUtil.stringToIntIfErrorReturnMinusOne(number);
            }
        }
        return 1;
    }

    private String parsePrices(Element itemBlock) {
        return itemBlock.select(PRICE_CSS_SELECTOR).text();
    }

    public void processTextToPriceAvg(String input, MotherBoardHotLine mb) {
        input = input.replace("грн", "").trim();
        String[] parts = input.split("–");
        double num1;
        if (parts.length == 2) {
            num1 = ParseUtil.stringToDoubleIfErrorReturnMinusOne(
                    parts[0].trim().replace(" ", ""));
            double num2 = ParseUtil.stringToDoubleIfErrorReturnMinusOne(
                    parts[1].trim().replace(" ", ""));
            mb.setAvgPrice((num1 + num2) / 2);
        } else if (parts.length == 1) {
            num1 = ParseUtil.stringToDoubleIfErrorReturnMinusOne(
                    parts[0].trim().replace(" ", ""));
            mb.setAvgPrice(num1);
        } else {
            mb.setAvgPrice(0.00);
        }
    }

    private void parseAndSetManufacturerAndName(Element itemBlock, MotherBoardHotLine mb) {
        Element nameElement = itemBlock.select(NAME_CSS_SELECTOR).first();
        String text = nameElement != null ? nameElement.text().trim() : "Не найдено";
        String manufacturer = text.split(" ")[0];
        String name = text.substring(manufacturer.length()).trim();
        mb.setManufacturer(manufacturer);
        mb.setName(name);
    }

    private String parseUrl(Element itemBlock) {
        Element linkElement = itemBlock.select(LINK_CSS_SELECTOR).first();
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

            boolean isCheckMode = false;
            if (isCheckMode) {
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
