package com.example.parser;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ParserApplicationTests {

    @Test
    void contextLoads() {
    }

    @SneakyThrows
    @Test
    void openFireFoxManualDriver() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Artem\\Documents\\Java\\UltimateJetBrains\\tutorials\\ms1\\parser\\parser\\drivers\\geckodriver.exe");

        WebDriver driver = new FirefoxDriver();
        driver.get("https://www.google.com");

        // Ваши тестовые действия здесь
        Thread.sleep(10000);
        System.out.println("Title: " + driver.getTitle());
        driver.quit();
    }

    @Test
    void openMozilla() {
        WebDriver driver = new FirefoxDriver();
        driver.get("https://google.com");
        System.out.println("Title: " + driver.getTitle());
        driver.quit();
    }

    @Test
    void openChromeDriver() {
        WebDriver driver = new ChromeDriver();
        driver.get("https://google.com");
        System.out.println("Title: " + driver.getTitle());
        driver.quit();
    }
}
