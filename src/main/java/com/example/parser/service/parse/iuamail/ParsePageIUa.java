package com.example.parser.service.parse.iuamail;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParsePageIUa {
    private final EnterCredentialIUa enterCredentialIUa;
    private final static By XPATH_SELECT_ALL = By.xpath("//span[@class = 'button l_r']/input[@type='checkbox']");
    private final static By XPATH_DELETE_BUTTON = By.xpath("/html/body/div[1]/div[6]/div[2]/div[2]/div[1]/div/fieldset[3]/span");


    public void start() {
        WebDriver driver = new ChromeDriver();
        enterCredentialIUa.enter(driver);
        for (int i = 0; i < 6; i++) {
            deleteMail100(driver);
        }
    }

    public void deleteMail100(WebDriver driver){
        driver.findElement(XPATH_SELECT_ALL).click();
        driver.findElement(XPATH_DELETE_BUTTON).click();
        Alert alert = driver.switchTo().alert();
        alert.accept();
        System.out.println("Окно обработано успешно!");
    }
}
