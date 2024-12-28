package com.example.parser.service.other;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebScraperSelenium {
    public static void main(String[] args) {
        // Укажите путь к драйверу
        System.setProperty("webdriver.chrome.driver", "C:\\path\\to\\chromedriver.exe");

        // Создаём экземпляр WebDriver
        WebDriver driver = new ChromeDriver();

        try {
            // Переход на страницу
            driver.get("https://hotline.ua/ua/computer/processory/");

            // Находим названия процессоров
            List<WebElement> processorNames = driver.findElements(By.cssSelector(".item-title"));

            // Находим цены процессоров
            List<WebElement> processorPrices = driver.findElements(By.cssSelector(".list-item__price .price__value"));

            // Выводим названия и цены
            for (int i = 0; i < processorNames.size(); i++) {
                String name = processorNames.get(i).getText();
                String price = i < processorPrices.size() ? processorPrices.get(i).getText() : "Цена не найдена";
                System.out.println("Процессор: " + name + " | Цена: " + price);
            }
        } finally {
            // Закрываем браузер
            driver.quit();
        }
    }
}