package com.example.parser.service.artline;

import com.example.parser.service.HtmlDocumentFetcher;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class ArtLineCpuParser {
    private final HtmlDocumentFetcher htmlDocumentFetcher;
    private static final String PARSING_PAGE_URL
            = "https://artline.ua/catalog/protsessory/page=";
    private static final int START_PAGE = 0;

    public void parseAllPagesWithProcessors(boolean isPrint) {
        int currentPage = START_PAGE;
        boolean isPageWithInformation = true;
        List<ArtLineCpu> processors = new ArrayList<>();

        /**
         * Find not empty pages and process it. If page is empty stop parsing, else
         * create document with information and parse it.
         * <div
         *     class="pagin"
         *     v-show="false">
         *     <a
         *         href="https://artline.ua/catalog/protsessory/page=2"
         *         class="pagin__indicator">
         *         1
         *     </a>
         * </div>div.pagin — выбирает все элементы <div> с классом pagin.
         * Пробел ( ) означает, что ищется вложенный элемент.
         * a.pagin__indicator — выбирает все элементы <a> с классом pagin__indicator,
         * которые находятся внутри <div class="pagin">.
         */
        while (isPageWithInformation) {
            currentPage++;
            log.info("Connect to the page: " + PARSING_PAGE_URL + currentPage);

            Document document = htmlDocumentFetcher.getHtmlDocumentAgent(
                    PARSING_PAGE_URL + currentPage,
                    true,
                    false,
                    false);

            Element linkElement = document.selectFirst("div.pagin a.pagin__indicator");
            if (linkElement != null) {
                processors.addAll(parseCurrentPage(document));
                log.info("Page " + currentPage + ". Parsing finished");
            } else {
                isPageWithInformation = false;
                log.info("Page: " + PARSING_PAGE_URL + currentPage + " is empty. Parsing processors finished");
            }
        }

        if (isPrint) {
            for (ArtLineCpu processor : processors) {
                System.out.println(processor);
            }
        }
    }

    private List<ArtLineCpu> parseCurrentPage(Document document) {
        /**
         * <div
         *     class="product-cart product-cart-js  "
         *     data-product-id="16648"
         *     data-url="https://artline.ua/product/protsessor-intel-core-i9-14900kf-24c8p16e-up-60ghz-36mb-lga1700-box"
         *     data-title="Процессор INTEL Core i9-14900KF (24C(8P+16E), up 6.0GHz, 36MB, LGA1700) BOX"
         *     data-is-hover-slider="0"
         * >
         * div — выбирает все элементы <div>.
         * .product-cart — фильтрует элементы <div>, оставляя только те,
         * которые имеют класс product-cart.
         * .product-cart-js — уточняет выборку, оставляя только те элементы,
         * которые имеют оба класса: product-cart и product-cart-js.
         */
        Elements productCarts = document.select("div.product-cart.product-cart-js");
        List<ArtLineCpu> processors = new ArrayList<>();

        for (Element productCart : productCarts) {
            String productId = productCart.attr("data-product-id");
            String productUrl = productCart.attr("data-url");
            String productTitle = productCart.attr("data-title");

            /**
             * <div class="product-cart__price">
             *     <div>21999 грн</div>
             *     <span class="product-cart__status">В наличии</span>
             * </div>
             */
            Element priceElement = productCart.selectFirst("div.product-cart__price > div");
            String price = priceElement != null ? priceElement.text() : "0.0";

            // Извлекаем информацию о наличии
            Element availabilityElement = productCart.selectFirst("div.product-cart__price > span");
            String availability = availabilityElement != null ? availabilityElement.text() : "Информация о наличии не указана";


            // Извлекаем ссылку и название товара из тега <a>
            /**
             *     <a href="https://artline.ua/product/protsessor-intel-core-i9-14900kf-24c8p16e-up-60ghz-36mb-lga1700-box" class="product-cart__title">
             *     Процессор INTEL Core i9-14900KF (24C(8P+16E), up 6.0GHz, 36MB, LGA1700) BOX
             *     </a>
             */
            Element titleElement = productCart.selectFirst("a.product-cart__title");
            String productUrlA = titleElement != null ? titleElement.attr("href") : "Ссылка не найдена";
            String productTitleA = titleElement != null ? titleElement.text() : "Название не указано";


            ArtLineCpu artlineCpu = new ArtLineCpu();
            artlineCpu.setProductId(productId);
            artlineCpu.setProductUrl(productUrl);
            artlineCpu.setProductTitle(productTitle);
            artlineCpu.setPrice(convertToDoubleAndCheckByNull(price));
            artlineCpu.setAvailability(availability.equals("В наличии"));
//            artlineProcessor.setProductUrlA(productUrlA);
//            artlineProcessor.setProductTitleA(productTitleA);

            processors.add(artlineCpu);
        }
        return processors;
    }

    private Double convertToDoubleAndCheckByNull(String price) {
        final String[] lineWithPrice = price.split(" ");
        final String stringPrice = lineWithPrice[0];
        double value = 0.0;
        try {
            return Double.parseDouble(stringPrice);
        } catch (NumberFormatException e) {
            return value;
        }
    }
}
