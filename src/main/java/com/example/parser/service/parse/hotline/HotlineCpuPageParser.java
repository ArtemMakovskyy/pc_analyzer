package com.example.parser.service.parse.hotline;

import com.example.parser.dto.hotline.UserBenchmarkShortCpuInfoDto;
import com.example.parser.model.hotline.CpuHotLine;
import com.example.parser.service.parse.HtmlDocumentFetcher;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class HotlineCpuPageParser {
    private final HtmlDocumentFetcher htmlDocumentFetcher;
    private static final String DOMAIN_LINK = "https://hotline.ua";
    private static final String BASE_URL = "https://hotline.ua/ua/computer/processory/?p=";
    private static final String TABLE_CSS_SELECTOR = "div.list-body__content.content.flex-wrap > div";

//    @PostConstruct
    public void parsePageFromFile() {
//        final List<CpuHotLine> cpuHotLines = pursePage(BASE_URL + 7);
//        cpuHotLines.forEach(System.out::println);
    }

    public List<CpuHotLine> purseAllPages() {
        int startPage = 1;
        int maxPage = findMaxPage();
        List<CpuHotLine> parts = new ArrayList<>();

        for (int i = startPage; i <= maxPage; i++) {
            final List<CpuHotLine> purse = pursePage(BASE_URL + i);
            parts.addAll(purse);
            log.info("... parsed page: " + i + " from: " + maxPage);
        }
        return parts;
    }


    public List<CpuHotLine> pursePage(String url) {
        Document htmlDocument = htmlDocumentFetcher.getHtmlDocumentFromWeb(
                url,
                true,
                true,
                false);

        Elements tableElements = htmlDocument.select(TABLE_CSS_SELECTOR);
        List<CpuHotLine> cpus = new ArrayList<>();

        if (tableElements.isEmpty()) {
            log.error("No data");
            throw new RuntimeException();
        } else {
            for (Element cpuBlock : tableElements) {
                CpuHotLine cpu = new CpuHotLine();
                cpu.setUrl(DOMAIN_LINK + parseUrl(cpuBlock));
                cpu.setName(parseName(cpuBlock));
                cpu.setPrices(parsePrices(cpuBlock));

                Element characteristicsBlock = cpuBlock.select("div.specs__text").first();
                parseDataFromCharacteristicsBlock(characteristicsBlock, cpu);

                cpus.add(cpu);
            }
        }

        return cpus;
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

    public List<CpuHotLine> pursePageOld(String url) {
        Document htmlDocument = htmlDocumentFetcher.getHtmlDocumentFromWeb(
                url,
                true,
                true,
                false);

        Elements elements = htmlDocument.select(".list-item");
        List<CpuHotLine> cpus = new ArrayList<>();

        for (Element item : elements) {
            CpuHotLine cpu = new CpuHotLine();

            Element titleElement = item.selectFirst(".list-item__title-container a.item-title");
            cpu.setName(titleElement != null ? titleElement.text() : "No data");

            cpu.setUrl(titleElement != null ? DOMAIN_LINK + (titleElement.attr("href")) : "No data");


            Element priceElement = item.selectFirst(".list-item__value-price");
            cpu.setPrices(priceElement != null ? priceElement.text()
                    .replace(" ", "").trim() : "Не указана");

            final UserBenchmarkShortCpuInfoDto userBenchmarkShortCpuInfoDto
                    = parseBaseCpuCharacteristics(elements);

            cpu.setSocketType(userBenchmarkShortCpuInfoDto.getSocketType());
            cpu.setFrequency(userBenchmarkShortCpuInfoDto.getFrequency());
            cpu.setL3Cache(userBenchmarkShortCpuInfoDto.getL3Cache());
            cpu.setCores(userBenchmarkShortCpuInfoDto.getCores());
            cpu.setThreads(userBenchmarkShortCpuInfoDto.getThreads());
            cpu.setPackageType(userBenchmarkShortCpuInfoDto.getPackageType());
            cpu.setReleaseDate(userBenchmarkShortCpuInfoDto.getReleaseDate());
            cpus.add(cpu);
        }
        return cpus;
    }

    private int findMaxPage() {
        Document htmlDocument = htmlDocumentFetcher.getHtmlDocumentFromWeb(
                BASE_URL + 1,
                true,
                true,
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

    private UserBenchmarkShortCpuInfoDto parseBaseCpuCharacteristics(Elements elements) {
        Elements specItems = elements.select(".spec-item");

        UserBenchmarkShortCpuInfoDto cpuInfo = new UserBenchmarkShortCpuInfoDto();

        for (Element data : specItems) {
            String key = data.select(".text-gray").text();
            String value = data.ownText().trim();

            switch (key) {
                case "Роз'єм:" -> cpuInfo.setSocketType(value);
                case "Частота:" -> cpuInfo.setFrequency(value);
                case "Кеш третього рівня:" -> cpuInfo.setL3Cache(value);
                case "Кількість ядер:" -> cpuInfo.setCores(value);
                case "Число потоків:" -> cpuInfo.setThreads(value);
                case "Комплектація:" -> cpuInfo.setPackageType(value);
                case "Рік" -> cpuInfo.setReleaseDate(value);
            }
        }
        return cpuInfo;
    }

    private CpuHotLine parseInnerCpuCharacteristics(String url) {

        Document htmlDocument = htmlDocumentFetcher.getHtmlDocumentFromWeb(
                url,
                true,
                true,
                false);

        Elements rows = htmlDocument.select(
                "table.specifications__table tr");

        CpuHotLine cpu = new CpuHotLine();

        for (Element row : rows) {
            Elements columns = row.select("td");

            // Escape line without data
            if (columns.size() < 2) continue;

            String key = columns.get(0).text().trim();
            String value = columns.get(1).text().trim();

            switch (key) {
                case "Бренд:" -> cpu.setBrand(value);
                case "Тип:" -> cpu.setType(value);
                case "Тип роз'єму:" -> cpu.setSocketType(value);
                case "Базова частота продуктивних ядер, ГГц:" ->
                        cpu.setBaseFrequency(Double.parseDouble(value.replace(",", ".")));
                case "Максимальна частота продуктивних ядер, ГГц:" ->
                        cpu.setMaxFrequency(Double.parseDouble(value.replace(",", ".")));
                case "Назва ядра:" -> cpu.setCoreName(value);
                case "Загальна кількість ядер:" -> cpu.setCoreCount(Integer.parseInt(value));
                case "Кількість потоків:" -> cpu.setThreadCount(Integer.parseInt(value));
            }
        }
        return cpu;
    }
}
