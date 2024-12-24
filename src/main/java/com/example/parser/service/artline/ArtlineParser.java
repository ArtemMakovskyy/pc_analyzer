package com.example.parser.service.artline;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ArtlineParser {
    private static final String PARSING_PAGE_URL
            = "https://artline.ua/catalog/protsessory/page=";


    @PostConstruct
    public void init() throws IOException {
        log.info("ArtlineParser");
//        parse();
        parseAllPagesWithProcessors();
    }

    @SneakyThrows
    public void parseAllPagesWithProcessors() {
        int currentPage = 0;
        boolean isPageWithInformation = true;

        /**
         * Find not empty pages and process it. If page is empty stop parsing
         * Create document with information and get it.
         */
        while (isPageWithInformation) {
            currentPage++;
            log.info("Подключение к главной странице " + PARSING_PAGE_URL + currentPage);
            Document document = Jsoup.connect(PARSING_PAGE_URL + currentPage).get();
            Element linkElement = document.selectFirst("div.pagin a.pagin__indicator");
            if (linkElement != null) {
                log.info("Page " + currentPage + ". Parsing started");
                parseCurrentPage(document);
            } else {
                isPageWithInformation = false;
            }
            log.info("Page: " + PARSING_PAGE_URL + currentPage + " is empty. Parsing processors finished");
        }
    }

    private Document getDocument() throws IOException {
        log.info("Подключение к главной странице " + PARSING_PAGE_URL);
        Document document = Jsoup.connect(PARSING_PAGE_URL).get();
        return document;
    }

    public void parse() throws IOException {
        openUriAndUseElementsByAttributeValue(true);
    }

    @SneakyThrows
    private void openUriAndUseElementsByAttributeValue(boolean prnDocument) {
        /**
         *
         */
//        a1(getDocument());
        parseCurrentPage(getDocument());
    }


    private void parseCurrentPage(Document document) {
        /**
         * <div
         *     class="product-cart product-cart-js  "
         *     data-product-id="16648"
         *     data-url="https://artline.ua/product/protsessor-intel-core-i9-14900kf-24c8p16e-up-60ghz-36mb-lga1700-box"
         *     data-title="Процессор INTEL Core i9-14900KF (24C(8P+16E), up 6.0GHz, 36MB, LGA1700) BOX"
         *     data-is-hover-slider="0"
         * >
         */
        Elements productCarts = document.select("div.product-cart.product-cart-js");
        for (Element productCart : productCarts) {
            // Извлекаем нужные данные
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
            String price = priceElement != null ? priceElement.text() : "Цена не указана";

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


            System.out.println("Product ID: " + productId);
            System.out.println("Product URL: " + productUrl);
            System.out.println("Product Title: " + productTitle);

            System.out.println("Price: " + price.split(" ")[0]);
            System.out.println("Availability: " + availability);
            System.out.println("productUrlA: " + productUrlA);
            System.out.println("productTitleA: " + productTitleA);
            System.out.println("-------------------------------------------------");


        }
    }

    private void a2HtmlBlock() {
        /**
         * <div
         *     class="product-cart product-cart-js  "
         *     data-product-id="16648"
         *     data-url="https://artline.ua/product/protsessor-intel-core-i9-14900kf-24c8p16e-up-60ghz-36mb-lga1700-box"
         *     data-title="Процессор INTEL Core i9-14900KF (24C(8P+16E), up 6.0GHz, 36MB, LGA1700) BOX"
         *     data-is-hover-slider="0"
         * >
         */
    }

    private void a1(Document document) {

        final Elements elementsByClass = document.getElementsByClass("product-cart__title");

        elementsByClass.forEach(System.out::println);

        for (Element element : elementsByClass) {
            System.out.println(element.text() + " | " + element.attr("href"));
        }
    }


}
