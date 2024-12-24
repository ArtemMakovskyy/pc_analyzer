package com.example.parser.info;

import java.io.File;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class InfoJsoup {
    private static final String LINK1 = "https://javarush.com/ua/groups/posts/uk.2767.parsing-html-bblotekoju-sp";
    private static final String LINK2 = "https://ru-brightdata.com/blog/how-tos-ru/web-scraping-with-jsoup";


    @SneakyThrows
    public void info() {
        // Підключитись до сервера
        Document document1_1 = Jsoup.connect("https://hh.ru/").get();

        Document document1_2 = Jsoup
                .connect("https://quotes.toscrape.com/")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36")
                .get();

        // З файлу
        File file = new File("hh-test.html");
        Document document2 = Jsoup.parse(file, "UTF-8", "hh.ru");


    }

}
