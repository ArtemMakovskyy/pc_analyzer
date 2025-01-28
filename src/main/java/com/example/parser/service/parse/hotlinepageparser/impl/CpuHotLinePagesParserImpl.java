package com.example.parser.service.parse.hotlinepageparser.impl;

import com.example.parser.model.hotline.CpuHotLine;
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
public class CpuHotLinePagesParserImpl extends HotLinePagesParserAbstract<CpuHotLine> {
    private static final String BASE_URL = "https://hotline.ua/ua/computer/processory/?p=";
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

    public CpuHotLinePagesParserImpl(HtmlDocumentFetcher htmlDocumentFetcher) {
        super(htmlDocumentFetcher, BASE_URL);
    }

    @Override
    protected List<CpuHotLine> parseData(Document htmlDocument) {
        Elements tableElements = htmlDocument.select(TABLE_CSS_SELECTOR);
        List<CpuHotLine> cpus = new ArrayList<>();

        for (Element itemBlock : tableElements) {
            CpuHotLine cpu = new CpuHotLine();
            setFields(cpu, itemBlock);
            cpus.add(cpu);
        }

        return cpus;
    }

    private void setFields(CpuHotLine cpu, Element itemBlock) {
        cpu.setUrl(DOMAIN_LINK + parseUrl(itemBlock));
        cpu.setPropositionsQuantity(setPropositionQuantity(itemBlock));

        parseAndSetManufacturerAndName(itemBlock, cpu);

        String prices = parsePrices(itemBlock);
        cpu.setPrices(prices);
        processTextToPriceAvg(prices, cpu);
        Element characteristicsBlock
                = itemBlock.select(CHARACTERISTICS_BLOCK_CSS_SELECTOR).first();
        parseDataFromCharacteristicsBlock(characteristicsBlock, cpu);
    }

    private void parseAndSetManufacturerAndName(Element itemBlock, CpuHotLine cpu) {
        Element nameElement = itemBlock.select(NAME_CSS_SELECTOR).first();
        String text = nameElement != null ? nameElement.text().trim() : "Не найдено";
        String manufacturer = text.split(" ")[0];
        String name = text.substring(manufacturer.length()).trim();
        cpu.setManufacturer(manufacturer);
        cpu.setName(name);
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

    private String parsePrices(Element cpuBlock) {
        return cpuBlock.select(PRICE_CSS_SELECTOR).text();
    }

    public void processTextToPriceAvg(String input, CpuHotLine cpu) {
        input = input.replace("грн", "").trim();
        String[] parts = input.split("–");
        double num1;
        if (parts.length == 2) {
            num1 = ParseUtil.stringToDoubleIfErrorReturnMinusOne(
                    parts[0].trim().replace(" ", ""));
            double num2 = ParseUtil.stringToDoubleIfErrorReturnMinusOne(
                    parts[1].trim().replace(" ", ""));
            cpu.setAvgPrice((num1 + num2) / 2);
        } else if (parts.length == 1) {
            num1 = ParseUtil.stringToDoubleIfErrorReturnMinusOne(
                    parts[0].trim().replace(" ", ""));
            cpu.setAvgPrice(num1);
        } else {
            cpu.setAvgPrice(0.00);
        }
    }

    private String parseUrl(Element cpuBlock) {
        Element linkElement = cpuBlock.select(LINK_CSS_SELECTOR).first();
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
