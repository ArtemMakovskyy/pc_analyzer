package com.example.parser.service.other;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class WebScraper2 {

    @SneakyThrows
    public static void main(String[] args) {
        Thread thread1 = new Thread(WebScraper2::pechi);
        thread1.start();
        thread1.join();
    }

    private static void pechi() {
//        List<String> processorNames = new ArrayList<>();
        try {
            String url = "https://hotline.ua/ua/computer/processory/20273844/";
            // Загрузка страницы
            Document doc = Jsoup.connect(url).get();
            System.out.println(doc);

            // Селектор для названий процессоров
            Elements processorNames = doc.select(".item-title");

            // Селектор для цен
            Elements processorPrices = doc.select(".list-item__price .price__value");

            // Карта для хранения соответствия "название -> цена"
            Map<String, String> processorMap = new HashMap<>();

            // Итерация по названиям и ценам
            for (int i = 0; i < processorNames.size(); i++) {
                String name = processorNames.get(i).text();
                String price = i < processorPrices.size() ? processorPrices.get(i).text() : "Цена не найдена";
                processorMap.put(name, price);
            }

            // Вывод данных
            for (Map.Entry<String, String> entry : processorMap.entrySet()) {
                System.out.println("Процессор: " + entry.getKey() + " | Цена: " + entry.getValue());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}