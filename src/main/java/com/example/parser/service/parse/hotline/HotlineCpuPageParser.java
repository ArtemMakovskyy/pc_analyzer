package com.example.parser.service.parse.hotline;

import com.example.parser.model.hotline.CpuHotLine;
import com.example.parser.dto.hotline.UserBenchmarkShortCpuInfoDto;
import com.example.parser.service.parse.HtmlDocumentFetcher;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class HotlineCpuPageParser {
    private final HtmlDocumentFetcher htmlDocumentFetcher;
    private static final String DOMAIN_LINK = "https://hotline.ua";
    private static final String ABOUT_PRODUCT = "?tab=about";
    private static final String BASE_URL = "https://hotline.ua/ua/computer/processory/?p=";

    public List<CpuHotLine> purseAllPages() {
        int startPage = 1;
        int maxPage = findMaxPage();
        List<CpuHotLine> parts = new ArrayList<>();

        for (int i = startPage; i <= maxPage; i++) {
            final List<CpuHotLine> purse = pursePage(BASE_URL + i);
            parts.addAll(purse);
            log.info("... parsed page: " + i);
        }
        return parts;
    }

    public List<CpuHotLine> pursePage(String url) {
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

            // Извлекаем характеристики. Зависаэ
//            final Cpu cpu = parseInnerCpuCharacteristics(DOMEN_LINK + url + ABOUT_PRODUCT);

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
