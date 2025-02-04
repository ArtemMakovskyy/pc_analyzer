package com.example.parser.service.parse.hotlinepageparser.impl;

import com.example.parser.dto.hotline.PowerSupplierHotLineParserDto;
import com.example.parser.service.parse.HtmlDocumentFetcher;
import com.example.parser.service.parse.utils.ParseUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class PowerSupplierHotLinePagesParserImpl
        extends HotLinePagesParserAbstract<PowerSupplierHotLineParserDto> {
    private static final String BASE_URL
            = "https://hotline.ua/ua/computer/bloki-pitaniya/296278-296291-296313-296359-296368/?p=";
    private static final String CHARACTERISTICS_BLOCK_CSS_SELECTOR = "div.specs__text";
    private static final String PRICE_CSS_SELECTOR = "div.list-item__value-price";
    private static final String NAME_CSS_SELECTOR = "div.list-item__title-container a";
    private static final String LINK_CSS_SELECTOR = "a.item-title.link--black";
//    private static final String PROPOSITION_QUANTITY_CSS_SELECTOR
//            = "a.link.link--black.text-sm.m_b-5";
//    private static final String NO_ELEMENT_CSS_SELECTOR
//            = "div.list-item__value > div.list-item__value--overlay."
//            + "list-item__value--full > div > div > div.m_b-10";
//    private static final String DIGITS_REGEX = "\\d+";

    public PowerSupplierHotLinePagesParserImpl(HtmlDocumentFetcher htmlDocumentFetcher) {
        super(htmlDocumentFetcher, BASE_URL);
    }

//    @PostConstruct
    public void init(){
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(availableProcessors);
        parseAllMultiThread(executor );
    }

    @Override
    protected List<PowerSupplierHotLineParserDto> parseData(Document htmlDocument) {
        Elements tableElements = htmlDocument.select(TABLE_CSS_SELECTOR);
        List<PowerSupplierHotLineParserDto> powerSuppliersList = new ArrayList<>();

        for (Element itemBlock : tableElements) {
            PowerSupplierHotLineParserDto ps = new PowerSupplierHotLineParserDto();
            setFields(ps, itemBlock);
            powerSuppliersList.add(ps);
        }

        return powerSuppliersList;
    }

    private void setFields(PowerSupplierHotLineParserDto powerSupplierHotLine, Element itemBlock) {
        powerSupplierHotLine.setUrl(DOMAIN_LINK + parseUrl(itemBlock));
        powerSupplierHotLine.setPropositionsQuantity(setPropositionQuantity(itemBlock));
        parseAndSetManufacturerAndName(itemBlock, powerSupplierHotLine);
        String prices = parsePrices(itemBlock);
        powerSupplierHotLine.setPrices(prices);
        processTextToPriceAvg(prices, powerSupplierHotLine);
        Element characteristicsBlock
                = itemBlock.select(CHARACTERISTICS_BLOCK_CSS_SELECTOR).first();
        parseDataFromCharacteristicsBlock(characteristicsBlock, powerSupplierHotLine);
    }

//    private int setPropositionQuantity(Element itemBlock) {
//        Elements noElement = itemBlock.select(NO_ELEMENT_CSS_SELECTOR);
//        if (!noElement.isEmpty()) {
//            String waitingText = noElement.text();
//
//            if (waitingText.contains("Очікується в продажу")) {
//                return 0;
//            }
//        }
//
//        Elements propositionQuantityElement = itemBlock.select(PROPOSITION_QUANTITY_CSS_SELECTOR);
//        if (!propositionQuantityElement.isEmpty()) {
//            String text = propositionQuantityElement.text().trim();
//            Pattern pattern = Pattern.compile(DIGITS_REGEX);
//            Matcher matcher = pattern.matcher(text);
//
//            if (matcher.find()) {
//                String quantity = matcher.group();
//                return ParseUtil.stringToIntIfErrorReturnMinusOne(quantity);
//            }
//        }
//        return 1;
//    }

    private String parsePrices(Element itemBlock) {
        return itemBlock.select(PRICE_CSS_SELECTOR).text();
    }

    private void processTextToPriceAvg(String input, PowerSupplierHotLineParserDto powerSupplierHotLine) {
        input = input.replace("грн", "").trim();
        String[] parts = input.split("–");
        double num1;
        if (parts.length == 2) {
            num1 = ParseUtil.stringToDoubleIfErrorReturnMinusOne(
                    parts[0].trim().replace(" ", ""));
            double num2 = ParseUtil.stringToDoubleIfErrorReturnMinusOne(
                    parts[1].trim().replace(" ", ""));
            powerSupplierHotLine.setAvgPrice(calculateBestAvgPrice(num1, num2));
        } else if (parts.length == 1) {
            num1 = ParseUtil.stringToDoubleIfErrorReturnMinusOne(
                    parts[0].trim().replace(" ", ""));
            powerSupplierHotLine.setAvgPrice(num1);
        } else {
            powerSupplierHotLine.setAvgPrice(0.00);
        }
    }

    private void parseAndSetManufacturerAndName(Element itemBlock, PowerSupplierHotLineParserDto powerSupplierHotLine) {
        Element nameElement = itemBlock.select(NAME_CSS_SELECTOR).first();
        String text = nameElement != null ? nameElement.text().trim() : "Не найдено";
        String manufacturer = text.split(" ")[0];
        String name = text.substring(manufacturer.length()).trim();
        powerSupplierHotLine.setManufacturer(manufacturer);
        powerSupplierHotLine.setName(name);
    }

    private String parseUrl(Element itemBlock) {
        Element linkElement = itemBlock.select(LINK_CSS_SELECTOR).first();
        if (linkElement == null) {
            log.warn("Can't find url");
            return "";
        }
        return linkElement.attr("href");
    }

    private void parseDataFromCharacteristicsBlock(
            Element characteristicsBlock, PowerSupplierHotLineParserDto powerSupplierHotLine) {
        if (characteristicsBlock == null) {
            log.warn("Can't find span element to parse data");
        } else {
            Elements positions = characteristicsBlock.select("span");

            boolean isCheckMode = false;
            if (isCheckMode) {
                iterateSpanCheckMode(positions);
            }

            for (Element position : positions) {
                extractData(position.text(), powerSupplierHotLine);
            }
        }
    }

    private void iterateSpanCheckMode(Elements positions) {
        for (int i = 0; i < positions.size(); i++) {
            System.out.println(i + " " + positions.get(i).text());
        }
    }

    private void extractData(String text, PowerSupplierHotLineParserDto powerSupplierHotLine) {
        if (text.contains("ATX")) {
            powerSupplierHotLine.setType(
                    text
            );
        } else if (text.contains("Потужність: ")) {
            powerSupplierHotLine.setPower(
                    ParseUtil.stringToIntIfErrorReturnMinusOne(
                            splitAndExtractDataByIndex(text, 1)
                    )
            );
        } else if (text.contains("Стандарт: ")) {
            powerSupplierHotLine.setStandard(
                    text.replace("Стандарт: ", "")
            );
        } else if (text.contains("Підключення до материнської плати: ")) {
            powerSupplierHotLine.setMotherboardConnection(
                    text.replace("Підключення до материнської плати: ", "")
            );
        } else if (text.contains("Підключення відеокарт: ")) {
            powerSupplierHotLine.setGpuConnection(
                    text.replace("Підключення відеокарт: ", "")
            );
        }

    }

//    private String splitAndExtractDataByIndex(String text, int index) {
//        String[] textArray = text.split(" ");
//        if (textArray.length < index) {
//            log.warn(this.getClass() + ": Invalid index "
//                    + index + ". Text array length is " + textArray.length);
//            return "";
//        }
//        return textArray[index];
//    }

}
