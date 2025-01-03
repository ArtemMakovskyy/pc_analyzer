package com.example.parser.service.parse.pda;

import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.SneakyThrows;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;

//@Service
public class GetSeleniumDriver {

    @SneakyThrows
    @PostConstruct
    public void startItAuto() {
//        WebDriver driver = new FirefoxDriver();
        WebDriver driver = new ChromeDriver();
        driver.get("https://4pda.to/");

        System.out.println("Title: " + driver.getTitle());

        final String pageSource = driver.getPageSource();

        for (int i = 2; i <= 5; i++) {
            final WebElement paginationButton
                    = driver.findElement(
                            new By.ByXPath("//*[@id=\"uTah4xRPFw\"]/ul/li[" + i + "]/a"));
            paginationButton.click();
            Thread.sleep(200);
        }
        driver.quit();
    }
}
