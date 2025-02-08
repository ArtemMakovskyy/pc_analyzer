package com.example.parser;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ParserApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void openChromeDriver() {
        WebDriver driver = new ChromeDriver();
        driver.get("https://google.com");
        System.out.println("Title: " + driver.getTitle());
        driver.quit();
    }

}
