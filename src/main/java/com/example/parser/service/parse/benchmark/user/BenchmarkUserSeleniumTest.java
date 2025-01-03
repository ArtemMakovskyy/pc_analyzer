package com.example.parser.service.parse.benchmark.user;

import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;

//@Service
@Log4j2
public class BenchmarkUserSeleniumTest {
    private final static String PUSH_CHECK_BUTTON = "//*[@id=\"j_idt45:j_idt48:2:j_idt49\"]/i";
    @SneakyThrows
//    @PostConstruct
    public void startItAuto() {
//        WebDriver driver = new FirefoxDriver();
        WebDriver driver = new ChromeDriver();
        driver.get("https://cpu.userbenchmark.com/");

        System.out.println("Title: " + driver.getTitle());

        final String pageSource = driver.getPageSource();

        for (int i = 2; i <= 5; i++) {

            final WebElement paginationButton
                    = driver.findElement(
                    new By.ByXPath("//*[@id=\"tableDataForm:j_idt260\"]"));
            paginationButton.click();
            Thread.sleep(2000);
        }
//        driver.quit();
    }

    public void checkPageIdentity(){
        WebDriver driver = new ChromeDriver();
        driver.get("https://cpu.userbenchmark.com/");


        //todo if page has /fa fa-4x fa-male/
//        <i class="fa fa-4x fa-male"></i>
//        push button PUSH_CHECK_BUTTON
//        else read page

        try {
            String pageForParsing = "https://cpu.userbenchmark.com/";
            String testInnerPageForParsing = "https://cpu.userbenchmark.com/";
            // Открываем страницу
            driver.get(pageForParsing);

            By iconLocator = By.cssSelector("i.fa.fa-4x.fa-male");
            try {
                WebElement iconElement = driver.findElement(iconLocator);
                iconElement.click();
                log.info("No page check");
                System.out.println("Элемент найден и на него нажали.");
            } catch (NoSuchElementException e) {
                // Если элемент не найден, выводим сообщение
                System.out.println("Страница готова для парсинга.");
            }
        } finally {
            driver.quit();
        }

    }
}

