package com.example.parser.service.parse.hotlinepageparser.impl;

import com.example.parser.model.hotline.GpuHotLine;
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

@Component
@Log4j2
public class GpuHotLinePagesParserImpl extends HotLinePagesParserAbstract<GpuHotLine> {
    private static final String BASE_URL = "https://hotline.ua/ua/computer/videokarty/?p=";
    private static final String CHARACTERISTICS_BLOCK_CSS_SELECTOR = "div.specs__text";
    private static final String NO_ELEMENT_CSS_SELECTOR
            = "div.list-item__value > div.list-item__value--overlay."
            + "list-item__value--full > div > div > div.m_b-10";
    private static final String PROPOSITION_QUANTITY_CSS_SELECTOR
            = "a.link.link--black.text-sm.m_b-5";
    private static final String PRICE_CSS_SELECTOR = "div.list-item__value-price";
    private static final String NAME_CSS_SELECTOR = "div.list-item__title-container a";
    private static final String LINK_CSS_SELECTOR = "a.item-title.link--black";
    private static final String DIGITS_REGEX = "\\d+";

    public GpuHotLinePagesParserImpl(HtmlDocumentFetcher htmlDocumentFetcher) {
        super(htmlDocumentFetcher, BASE_URL);
    }

    @Override
    protected List<GpuHotLine> parseData(Document htmlDocument) {
        Elements tableElements = htmlDocument.select(TABLE_CSS_SELECTOR);
        List<GpuHotLine> gpus = new ArrayList<>();

        for (Element itemBlock : tableElements) {
            GpuHotLine gpu = new GpuHotLine();
            setFields(gpu, itemBlock);
            gpus.add(gpu);
        }
        return gpus;
    }

    private void setFields(GpuHotLine gpu, Element itemBlock) {
        gpu.setUrl(DOMAIN_LINK + parseUrl(itemBlock));
        gpu.setPropositionsQuantity(setPropositionQuantity(itemBlock));
        gpu.setName(parseName(itemBlock));
        String prices = parsePrices(itemBlock);
        gpu.setPrices(prices);
        processTextToPriceAvg(prices, gpu);
        Element characteristicsBlock
                = itemBlock.select(CHARACTERISTICS_BLOCK_CSS_SELECTOR).first();
        parseDataFromCharacteristicsBlock(characteristicsBlock, gpu);
    }

    private String parseUrl(Element itemBlock) {
        Element linkElement = itemBlock.select(LINK_CSS_SELECTOR).first();
        if (linkElement == null) {
            log.warn("Can't find url");
            return "";
        }
        return linkElement.attr("href");
    }

    private int setPropositionQuantity(Element itemBlock) {
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

    private String parseName(Element itemBlock) {
        Element nameElement = itemBlock.select(NAME_CSS_SELECTOR).first();
        return nameElement != null ? nameElement.text().trim() : "Не найдено";
    }

    private void parseDataFromCharacteristicsBlock(Element characteristicsBlock, GpuHotLine gpu) {
        if (characteristicsBlock == null) {
            log.warn("Can't find span element to parse data");
        } else {
            Elements positions = characteristicsBlock.select("span");

            boolean isCheckMode = false;
            if (isCheckMode) {
                iterateSpanCheckMode(positions);
            }

            for (Element position : positions) {
                extractData(position.text(), gpu);
            }
        }

    }

    private void iterateSpanCheckMode(Elements positions) {
        for (int i = 0; i < positions.size(); i++) {
            System.out.println(i + " " + positions.get(i).text());
        }
    }

    public void processTextToPriceAvg(String input, GpuHotLine gpu) {
        input = input.replace("грн", "").trim();
        String[] parts = input.split("–");
        double num1;
        if (parts.length == 2) {
            num1 = ParseUtil.stringToDoubleIfErrorReturnMinusOne(
                    parts[0].trim().replace(" ", ""));
            double num2 = ParseUtil.stringToDoubleIfErrorReturnMinusOne(
                    parts[1].trim().replace(" ", ""));
            gpu.setAvgPrice((num1 + num2) / 2);
        } else if (parts.length == 1) {
            num1 = ParseUtil.stringToDoubleIfErrorReturnMinusOne(
                    parts[0].trim().replace(" ", ""));
            gpu.setAvgPrice(num1);
        } else {
            gpu.setAvgPrice(0.00);
        }

    }

    private void extractData(String text, GpuHotLine gpu) {
        String textUpperCase = text.trim().toUpperCase();
        if (textUpperCase.contains("NVIDIA")
                || textUpperCase.contains("INTEL")
                || textUpperCase.contains("AMD")) {
            String manufacturer = splitAndExtractDataByIndex(text, 0);
            gpu.setManufacturer(
                    manufacturer
            );
            gpu.setName(text.replace(manufacturer, "").trim());
        } else if (text.contains("GDDR")) {
            gpu.setMemoryType(
                    text
            );
        } else if (text.contains("ГБ")) {
            gpu.setMemorySize(
                    splitAndExtractDataByIndex(text, 0)
            );
        } else if (text.contains("Шина: ")) {
            gpu.setShina(
                    text
            );
        } else if (text.contains("PCI Express")) {
            gpu.setPort(
                    text
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