package com.example.parser.service.parse.other;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class DocumentExample {

    public void parse() throws IOException {
        log.info("DocumentExample");
        byTag();
        byId();
        byClass();
        byAttribute();
        byAttributeValue();
        byCssSelector();
        byBody();
        byTitle();
        byText();
//        byOuterHtml();
    }

    public void byTag() throws IOException {
        printMethodName();
        /**
         * <html>
         *     <body>
         *         <div>Content</div>
         *         <p>Paragraph 1</p>
         *         <p>Paragraph 2</p>
         *     </body>
         * </html>
         */
        Elements paragraphs = document()
                .getElementsByTag("p");
        paragraphs.forEach(p -> System.out.println(p.text()));
    }

    public void byId() throws IOException {
        printMethodName();
        /**
         * <html>
         *     <body>
         *         <div id="header">Header</div>
         *         <div id="content">Main Content</div>
         *     </body>
         * </html>
         */

        Element header = document()
                .getElementById("header");
        System.out.println(header.text());
    }

    public void byClass() throws IOException {
        printMethodName();
        /**
         * <html>
         *     <body>
         *         <div class="menu">Menu</div>
         *         <div class="menu">Another Menu</div>
         *     </body>
         * </html>
         */
        Elements menus = document()
                .getElementsByClass("menu");
        menus.forEach(menu -> System.out.println(menu.text()));
    }

    public void byAttribute() throws IOException {
        printMethodName();
        /**
         * <html>
         *     <body>
         *         <div custom-attr="value1">First</div>
         *         <div custom-attr="value2">Second</div>
         *     </body>
         * </html>
         */
        Elements customElements = document()
                .getElementsByAttribute("custom-attr");
        customElements.forEach(element -> System.out.println(element.text()));
    }

    public void byAttributeValue() throws IOException {
        printMethodName();
        /**
         * <html>
         *     <body>
         *         <div data-type="header">Header</div>
         *         <div data-type="footer">Footer</div>
         *     </body>
         * </html>
         */
        Elements headers = document()
                .getElementsByAttributeValue("data-type", "header");
        headers.forEach(header -> System.out.println(header.text()));
    }

    public void byCssSelector() throws IOException {
        printMethodName();
        /**
         * <html>
         *     <body>
         *         <div class="container">
         *             <p class="text">Hello</p>
         *             <p class="text">World</p>
         *         </div>
         *     </body>
         * </html>
         */
        Elements texts = document()
                .select(".container .text");
        texts.forEach(text -> System.out.println(text.text()));
    }

    public void byBody() throws IOException {
        printMethodName();
        /**
         * <html>
         *     <body>
         *         <div>Body Content</div>
         *     </body>
         * </html>
         */
        Element body = document()
                .body();
        System.out.println(body.text());
    }

    public void byTitle() throws IOException {
        printMethodName();
        /**
         * <html>
         *     <head>
         *         <title>My Page</title>
         *     </head>
         *     <body>
         *         <div>Content</div>
         *     </body>
         * </html>
         */
        String title = document()
                .title();
        System.out.println(title);
    }

    public void byText() throws IOException {
        printMethodName();
        /**
         * <html>
         *     <body>
         *         <div>Text 1</div>
         *         <p>Text 2</p>
         *     </body>
         * </html>
         */
        String text = document()
                .text();
        System.out.println(text);
    }

    public void byOuterHtml() throws IOException {
        printMethodName();
        /**
         * <html>
         *     <body>
         *         <div>Example</div>
         *     </body>
         * </html>
         */
        String html = document()
                .outerHtml();
        System.out.println(html);
    }


    private Document document() throws IOException {
        final Document document = Jsoup.parse(
                new File("src/main/resources/static/other/tag-ex.html"));
        return document;
    }

    private void printMethodName() {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        System.out.println("       <<<<< Current method: " + methodName);
    }
}
