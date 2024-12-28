package com.example.parser.service.hotline;

import com.example.parser.model.hotline.Cpu;
import com.example.parser.model.hotline.ShortCpuInfoDto;
import com.example.parser.service.HtmlDocumentFetcher;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class HotlineCpuPageParser {
    private static final String DOMAIN_LINK = "https://hotline.ua";
    private static final String ABOUT_PRODUCT = "?tab=about";
    private static final String BASE_URL = "https://hotline.ua/ua/computer/processory/?p=";

    @PostConstruct
    public void init() {
//        System.out.println(purseAllPages().size());
//        final List<Cpu> cpus = purseAllPages();
//        cpus.forEach(System.out::println);
//        System.out.println(cpus.size());
    }

    public List<Cpu> purseAllPages() {
        int startPage = 1;
        int maxPage = findMaxPage();
        List<Cpu> parts = new ArrayList<>();

        for (int i = startPage; i <= maxPage; i++) {
            final List<Cpu> purse = pursePage(BASE_URL + i);
            parts.addAll(purse);
            log.info("Connected to the page: " + BASE_URL + i + ", and parsed it.");
        }
        return parts;
    }

    public List<Cpu> pursePage(String url) {
        final Document htmlDocument =
                HtmlDocumentFetcher.getInstance().getHtmlDocumentAgent(
                        false,
                        url);

        Elements elements = htmlDocument.select(".list-item");
        List<Cpu> cpus = new ArrayList<>();

        for (Element item : elements) {
            Cpu cpu = new Cpu();

            Element titleElement = item.selectFirst(".list-item__title-container a.item-title");
            cpu.setName(titleElement != null ? titleElement.text() : "No data");

            cpu.setUrl(titleElement != null ? DOMAIN_LINK + (titleElement.attr("href")) : "No data");

            // Извлекаем характеристики. Зависаэ
//            final Cpu cpu = parseInnerCpuCharacteristics(DOMEN_LINK + url + ABOUT_PRODUCT);

            Element priceElement = item.selectFirst(".list-item__value-price");
            cpu.setPrices(priceElement != null ? priceElement.text()
                    .replace(" ", "").trim() : "Не указана");

            final ShortCpuInfoDto shortCpuInfoDto
                    = parseBaseCpuCharacteristics(elements);

            cpu.setSocketType(shortCpuInfoDto.getSocketType());
            cpu.setFrequency(shortCpuInfoDto.getFrequency());
            cpu.setL3Cache(shortCpuInfoDto.getL3Cache());
            cpu.setCores(shortCpuInfoDto.getCores());
            cpu.setThreads(shortCpuInfoDto.getThreads());
            cpu.setPackageType(shortCpuInfoDto.getPackageType());
            cpu.setReleaseDate(shortCpuInfoDto.getReleaseDate());
            cpus.add(cpu);
        }
        return cpus;
    }

    private int findMaxPage() {
        Document document = HtmlDocumentFetcher.getInstance()
                .getHtmlDocumentAgent(false, BASE_URL + 1);

        Elements pages = document.select("a.page");
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

    private ShortCpuInfoDto parseBaseCpuCharacteristics(Elements elements) {
        Elements specItems = elements.select(".spec-item");

        ShortCpuInfoDto cpuInfo = new ShortCpuInfoDto();

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

    private Cpu parseInnerCpuCharacteristics(String url) {
        final Document documentProductSpecification
                = HtmlDocumentFetcher.getInstance().getHtmlDocumentAgent(false, url);

        Elements rows = documentProductSpecification.select(
                "table.specifications__table tr");

        Cpu cpu = new Cpu();

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
