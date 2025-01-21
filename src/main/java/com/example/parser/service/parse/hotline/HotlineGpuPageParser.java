package com.example.parser.service.parse.hotline;

import com.example.parser.model.hotline.GpuHotLine;
import com.example.parser.service.parse.HtmlDocumentFetcher;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class HotlineGpuPageParser {
    private final HtmlDocumentFetcher htmlDocumentFetcher;
    private static final String BASE_URL = "https://hotline.ua/ua/computer/videokarty/?p=";

    public List<GpuHotLine> purseAllPagesMultiThread(ExecutorService executor) {
        int startPage = 1;
        int maxPage = findMaxPage();
        List<GpuHotLine> parts = new ArrayList<>();
        List<Future<List<GpuHotLine>>> futures = new ArrayList<>();

        for (int i = startPage; i <= maxPage; i++) {
            int pageIndex = i;
            Future<List<GpuHotLine>> future = executor.submit(() -> {
                List<GpuHotLine> purse = pursePage(
                        BASE_URL + pageIndex,
                        true,
                        true,
                        8,
                        10,
                        false
                );
                log.info("... parsed page: " + pageIndex + " from: " + maxPage);
                return purse;
            });
            futures.add(future);
        }

        for (Future<List<GpuHotLine>> future : futures) {
            try {
                parts.addAll(future.get());
            } catch (InterruptedException | ExecutionException e) {
                log.error("Error parsing page", e);
                Thread.currentThread().interrupt();
            }
        }
        return parts;
    }

    public List<GpuHotLine> purseAllPages() {
        int startPage = 1;
        int maxPage = findMaxPage();
        List<GpuHotLine> parts = new ArrayList<>();

        for (int i = startPage; i <= maxPage; i++) {
            int pageIndex = i;
            final List<GpuHotLine> purse = pursePage(
                    BASE_URL + pageIndex,
                    true,
                    true,
                    2,
                    5,
                    false);
            parts.addAll(purse);
            log.info("Connected to the page: " + BASE_URL + i + ", and parsed it.");
        }
        return parts;
    }

    public List<GpuHotLine> pursePage(String url,
                                      boolean useUserAgent,
                                      boolean useDelay,
                                      int delayFrom,
                                      int delayTo,
                                      boolean isPrintDocumentToConsole) {
        final Document htmlDocument =
                htmlDocumentFetcher.getHtmlDocumentFromWeb(
                        url,
                        useUserAgent,
                        useDelay,
                        delayFrom,
                        delayTo,
                        isPrintDocumentToConsole);

        Elements elements = htmlDocument.select(".list-item");
        List<GpuHotLine> products = new ArrayList<>();

        for (Element item : elements) {
            GpuHotLine product = new GpuHotLine();
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
                .getHtmlDocumentFromWeb(
                        BASE_URL + 1,
                        false,
                        false,
                        false);

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
