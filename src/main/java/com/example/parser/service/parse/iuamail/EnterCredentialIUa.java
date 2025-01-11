package com.example.parser.service.parse.iuamail;

import com.example.parser.utils.ParseUtil;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnterCredentialIUa {
    private final static String MAIN_URL = "https://www.i.ua/";
    private final static String LOGIN = "photomak";
    private final static String PASSWORD = "ftofth96";
    private final static String DROPDOWN_TYPE = "i.ua";
    private final static By XPATH_LOGIN = By.xpath("//input[@name='login']");
    private final static By XPATH_PASSWORD = By.xpath("//input[@name='pass']");
    private final static By XPATH_DROPDOWN = By.xpath("//select[@name='domn']");
    private final static By XPATH_SUBMIT_BUTTON = By.xpath("//input[@title='Вхід на пошту']");


    public void enter(WebDriver driver) {
        driver.get(MAIN_URL);
        ParseUtil.applyRandomDelay(1,2,false);
        driver.findElement(XPATH_LOGIN).sendKeys(LOGIN);
        driver.findElement(XPATH_PASSWORD).sendKeys(PASSWORD);

        WebElement dropdown = driver.findElement(XPATH_DROPDOWN);
        Select select = new Select(dropdown);
        select.selectByValue(DROPDOWN_TYPE);
        ParseUtil.applyRandomDelay(1,2,false);
        driver.findElement(XPATH_SUBMIT_BUTTON).click();
    }
}
