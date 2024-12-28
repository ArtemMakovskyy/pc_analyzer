package com.example.parser.service.other;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebScraper {
    @SneakyThrows
    public static void main(String[] args) {
        Thread thread1 = new Thread(WebScraper::pechi);
        Thread thread2 = new Thread(WebScraper::posudomoechnoe_oborudovanie);
        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }

    private static void pechi() {
        List<GoodClassEntity>goodClassEntities = new ArrayList<>();
        try {
            // Подключение к веб-странице
//            String url = "https://maresto.ua/ua/catalog/konvektsionnye_pechi/shkaf_teplovoy/";
            String url = "https://maresto.ua/ua/catalog/konvektsionnye_pechi/";
            Document doc = Jsoup.connect(url).get();

//            System.out.println(doc);

            // Извлечение блоков с товарами
            Elements products = doc.select(".product__bot"); // Весь блок с названием и ценой

            for (Element product : products) {
                // Извлечение названия
                String title = product.select(".product__name a").text();

                // Извлечение текущей цены
                String price = product.select(".product__price-current h5").text();

                // Печать результата
//                System.out.println("Название: " + title + " | Цена: " + price);
                goodClassEntities.add(new GoodClassEntity(title,price));
            }
            System.out.println(goodClassEntities);
//            System.out.println(goodClassEntities.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void posudomoechnoe_oborudovanie() {
        List<GoodClassEntity>goodClassEntities = new ArrayList<>();
        try {
            // Подключение к веб-странице
//            String url = "https://maresto.ua/ua/catalog/konvektsionnye_pechi/shkaf_teplovoy/";
            String url = "https://maresto.ua/ua/catalog/posudomoechnoe_oborudovanie/";
            Document doc = Jsoup.connect(url).get();

//            System.out.println(doc);

            // Извлечение блоков с товарами
            Elements products = doc.select(".product__bot"); // Весь блок с названием и ценой

            for (Element product : products) {
                // Извлечение названия
                String title = product.select(".product__name a").text();

                // Извлечение текущей цены
                String price = product.select(".product__price-current h5").text();

                // Печать результата
//                System.out.println("Название: " + title + " | Цена: " + price);
                goodClassEntities.add(new GoodClassEntity(title,price));
            }
            System.out.println(goodClassEntities);
//            System.out.println(goodClassEntities.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Data
    @AllArgsConstructor
   static class GoodClassEntity{
        private String name;
        private String price;
    }
}