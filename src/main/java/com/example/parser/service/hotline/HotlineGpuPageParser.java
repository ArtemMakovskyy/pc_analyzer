package com.example.parser.service.hotline;

import com.example.parser.model.hotline.Gpu;
import com.example.parser.service.HtmlDocumentFetcher;
import jakarta.annotation.PostConstruct;
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
public class HotlineGpuPageParser {
    private final HtmlDocumentFetcher htmlDocumentFetcher;
    private static final String DOMAIN_LINK = "https://hotline.ua";
    private static final String ABOUT_PRODUCT = "?tab=about";
    private static final String BASE_URL = "https://hotline.ua/ua/computer/videokarty/?p=";

    public List<Gpu> purseAllPages() {
        int startPage = 1;
        int maxPage = findMaxPage();
        List<Gpu> parts = new ArrayList<>();

        for (int i = startPage; i <= maxPage; i++) {
            final List<Gpu> purse = pursePage(BASE_URL + i);
            parts.addAll(purse);
            log.info("Connected to the page: " + BASE_URL + i + ", and parsed it.");
        }
        return parts;
    }

    public List<Gpu> pursePage(String url) {
        final Document htmlDocument =
                htmlDocumentFetcher.getHtmlDocumentAgent(
                        url,
                        true,
                        true,
                        false);


        Elements elements = htmlDocument.select(".list-item");
        List<Gpu> products = new ArrayList<>();

        for (Element item : elements) {
            Gpu product = new Gpu();
            Element titleElement = item.selectFirst(".list-item__title-container a.item-title");
            product.setTotalName(titleElement != null ? titleElement.text() : "No data");

            Elements specs = item.select(".spec-item.spec-item--bullet");
            for (Element spec : specs) {
                if (spec == null) {
                    continue;
                }
                 String textUpperCase = spec.text().trim().toUpperCase();
                 String textOrigin = spec.text().trim();
                if (textUpperCase.contains("NVIDIA")
                        || textUpperCase.contains("INTEL")
                        || textUpperCase.contains("AMD")) {
                    product.setName(textOrigin);
                } else if (textOrigin.contains("GDDR")) {
                    product.setMemoryType(textOrigin);
                } else if (textOrigin.contains(" ГБ")) {
                    product.setMemorySize(textOrigin);
                } else if (textOrigin.contains("Шина: ")) {
                    product.setShina(textOrigin);
                } else if (textOrigin.contains("PCI Express")) {
                    product.setPort(textOrigin);
                }
            }
            final Element elementYear = item.selectFirst(
                    "span.spec-item:contains(Рік)");
            product.setYear((elementYear != null) ? elementYear.ownText().trim() : null);

            final Element elementPrice = item.selectFirst(
                    "div.list-item__value-price");


            if (elementPrice != null) {
                product.setPrice(elementPrice.text().replace("грн", "").trim());
            } else {
                product.setPrice(null);
            }

            products.add(product);
        }

        return products;
    }

    private int findMaxPage() {
        Document document = htmlDocumentFetcher
                .getHtmlDocumentAgent(false, BASE_URL + 1);

        Elements pages = document.select("a.page");
        int maxPage = 0;

        for (Element page : pages) {
            String pageText = page.text().trim();
            try {
                int pageNumber = Integer.parseInt(pageText);
                maxPage = Math.max(maxPage, pageNumber);
            } catch (NumberFormatException e) {
//                log.info("Cant get number with pageText: " + pageText + " Ignore it.");
            }
        }
        return maxPage;
    }
}
