package com.example.parser.service.parse.selenium;

import com.example.parser.utils.ParseUtil;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

@Service
public class LernSelenium {
    String lesson = "https://www.youtube.com/watch?v=yPZQ7h-J--c&list=PL3U4JD8S3lin3seHzfHlInqMCgzC_1gFW&index=40";
//    https://www.youtube.com/watch?v=Ic74ONESGB0&list=PL3U4JD8S3lin3seHzfHlInqMCgzC_1gFW&index=2
    /**
     * <a href="/resources">
     * <span>Ресурсы</span>
     * </a>
     * <p>
     * //span[text()='Ресурсы']/parent::a
     */

//    @PostConstruct
    public void start13_TransparentDriver() {
        // Настраиваем ChromeOptions для headless режима
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Запуск без графического интерфейса
        options.addArguments("--disable-gpu"); // Опционально, для улучшения совместимости
        options.addArguments("--window-size=1920,1080"); // Установка размера окна

        WebDriver driver = new ChromeDriver(options);
        driver.get("https://en.wikipedia.org/wiki/Main_Page");
        System.out.println(driver.getTitle());
        driver.quit();
    }

    public void start13_ScreenShot59() {
        WebDriver driver = new ChromeDriver();



        driver.get("https://en.wikipedia.org/wiki/Main_Page");
        String xPathSearchButton = "//input[@type='search']";
        final By xpathSearchBy = By.xpath(xPathSearchButton);
        final WebElement elementSearch = driver.findElement(xpathSearchBy);
        elementSearch.sendKeys("country ");
        elementSearch.sendKeys(Keys.ENTER);

        final File screenshotAs = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String linkSave = "C:\\Users\\Artem\\Documents\\Java\\UltimateJetBrains\\tutorials\\ms1\\parser\\parser\\src\\main\\java\\com\\example\\parser\\service\\parse\\selenium\\sc1.png";

        try {
            FileUtils.copyFile(screenshotAs, new File(linkSave));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        driver.quit();
    }


    public void start12_Keys() {
//        https://www.youtube.com/watch?v=vGpoA5Imi9Y&list=PL3U4JD8S3lin3seHzfHlInqMCgzC_1gFW&index=19
        WebDriver driver = new ChromeDriver();

        driver.get("https://en.wikipedia.org/wiki/Main_Page");
        String xPathSearchButton = "//input[@type='search']";
        final By xpathSearchBy = By.xpath(xPathSearchButton);
        final WebElement elementSearch = driver.findElement(xpathSearchBy);
        elementSearch.sendKeys("country ");
        elementSearch.sendKeys(Keys.chord(Keys.SHIFT, "ukraine"));

        String selectAll = Keys.chord(Keys.SHIFT, "a");
        String cut = Keys.chord(Keys.CONTROL, "x");
        String copy = Keys.chord(Keys.CONTROL, "c");
        String paste = Keys.chord(Keys.CONTROL, "v");

        elementSearch.sendKeys(selectAll);


//        elementSearch.sendKeys(Keys.ENTER);

//        driver.navigate().back();
//        driver.quit();

    }

    public void start11_ChickIfElementIs() {
        System.out.println("<<<<<<<<<<start11_ChickIfElementIs");
//        https://www.youtube.com/watch?v=gA4BTgbCODU&list=PL3U4JD8S3lin3seHzfHlInqMCgzC_1gFW&index=27
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.i.ua/");

        String xPathInsertButton = "//input[@value='Увійти']";
        String xPathWorkButton = "//a[@class = 'job_16']";

        final int sizeElementsInsertButtons
                = driver.findElements(new By.ByXPath(xPathInsertButton)).size();

        if (sizeElementsInsertButtons > 0) {
            System.out.println("Insert button can be clicked " + sizeElementsInsertButtons);
            driver.findElement(By.xpath(xPathInsertButton)).click();
        }
        System.out.println("<<<<<<< " + sizeElementsInsertButtons);


    }


    public void start10Windows_2AndKeys() {
//        https://www.youtube.com/watch?v=gA4BTgbCODU&list=PL3U4JD8S3lin3seHzfHlInqMCgzC_1gFW&index=27
        WebDriver driver = new ChromeDriver();
        try {

            // Открываем начальную страницу
            driver.get("https://www.oracle.com/cis/financial-services/");

            // Находим ссылку
            WebElement link = driver.findElement(By.xpath("//a[text()='Подробнее о решениях для страхования']"));

            // Открываем ссылку в новой вкладке
            String openInNewTab = Keys.chord(Keys.CONTROL, Keys.RETURN); // Для Windows (CTRL + Enter)
            link.sendKeys(openInNewTab);

            // Получаем все открытые вкладки
            ArrayList<String> windowsTabs
                    = new ArrayList<>(driver.getWindowHandles());

            // Переключаемся на новую вкладку
            driver.switchTo().window(windowsTabs.get(1));

            // Выполняем действия в новой вкладке
            System.out.println("Открыта новая вкладка: " + driver.getCurrentUrl());

            // Закрываем новую вкладку
            driver.close();

            // Переключаемся обратно на первую вкладку
            driver.switchTo().window(windowsTabs.get(0));

            // Проверяем текущий URL
            System.out.println("Вернулись на исходную вкладку: " + driver.getCurrentUrl());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Закрываем браузер
            driver.quit();
        }

    }

    public void start10Windows_AND_WORK_WITH_TEXT2() {
//        https://www.youtube.com/watch?v=gA4BTgbCODU&list=PL3U4JD8S3lin3seHzfHlInqMCgzC_1gFW&index=27
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.oracle.com/java/technologies/javase/javase8-archive-downloads.html");
        String mainWindow = driver.getWindowHandle();
        System.out.println(mainWindow);
//        String clickCode2 = "/html/body/div[2]/section[2]/div/section[1]/div/div/div/p/a";
//        String clickCode1 = "//a[contains(text(),'Oracle Binary Code License Agreement for Java SE Platform Products')]";
        String clickCode = "//a[text()='Oracle Binary Code License Agreement for Java SE Platform Products']";
        driver.findElement(By.xpath(clickCode)).click();

        for (String windowHandle : driver.getWindowHandles()) {
            driver.switchTo().window(windowHandle);
        }

        String codeInNextWindow = "//*[@id=\"licenseContent\"]/div/ol/li[7]/a";

        driver.findElement(By.xpath(codeInNextWindow)).click();
        driver.switchTo().window(mainWindow);
        String productButton = "//*[@id=\"products1\"]";
        driver.findElement(By.xpath(productButton)).click();
//        driver.quit();
    }


    public void start10Windows_AND_WORK_WITH_TEXT() {
//        https://www.youtube.com/watch?v=gA4BTgbCODU&list=PL3U4JD8S3lin3seHzfHlInqMCgzC_1gFW&index=27
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.oracle.com/java/technologies/javase/javase8-archive-downloads.html");
        String mainWindow = driver.getWindowHandle();
        System.out.println(mainWindow);
//        String clickCode2 = "/html/body/div[2]/section[2]/div/section[1]/div/div/div/p/a";
//        String clickCode1 = "//a[contains(text(),'Oracle Binary Code License Agreement for Java SE Platform Products')]";
        String clickCode = "//a[text()='Oracle Binary Code License Agreement for Java SE Platform Products']";
        driver.findElement(By.xpath(clickCode)).click();

        for (String windowHandle : driver.getWindowHandles()) {
            driver.switchTo().window(windowHandle);
        }

        String codeInNextWindow = "//*[@id=\"licenseContent\"]/div/ol/li[7]/a";

        driver.findElement(By.xpath(codeInNextWindow)).click();
        driver.switchTo().window(mainWindow);
        String productButton = "//*[@id=\"products1\"]";
        driver.findElement(By.xpath(productButton)).click();
//        driver.quit();
    }

    public void start9actions() {
//        https://www.youtube.com/watch?v=gA4BTgbCODU&list=PL3U4JD8S3lin3seHzfHlInqMCgzC_1gFW&index=27
        WebDriver driver = new ChromeDriver();
        driver.get("https://ebay.com/");
//        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);// явное ожидание
        By link = By.cssSelector("li.vl-flyout-nav__js-tab:nth-child(3) > a:nth-child(1)");
        final WebElement elementLink = driver.findElement(link);
        Actions actions = new Actions(driver);
        actions.moveToElement(elementLink).build().perform();


//        driver.quit();
    }


    public void start8() {
//        https://www.youtube.com/watch?v=VuXBo6UBFw4&list=PL3U4JD8S3lin3seHzfHlInqMCgzC_1gFW&index=27
        WebDriver driver = new FirefoxDriver();
        driver.get("https://www.google.com.ua/");
//        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);// явное ожидание
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        By xpath1 = By.xpath("//*[@id='SIvCob' and contains(text(), 'Сервисы Google доступны на этих языках')]");
        wait.until(ExpectedConditions.presenceOfElementLocated(xpath1));


//        driver.quit();
    }


    public void start7TAble() {
//        TABLE
        String lesson = "https://www.youtube.com/watch?v=ntri9SqBD7M&list=PL3U4JD8S3lin3seHzfHlInqMCgzC_1gFW&index=28";
        WebDriver driver = new FirefoxDriver();
        driver.get("https://www.w3schools.com/html/html_tables.asp");
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        final By tableElementXpath = By.xpath("//table[@id='customers']");
        final WebElement tableElement = driver.findElement(tableElementXpath);

        Table table = new Table(tableElement, driver);

//        System.out.println(table.getRows());
//        System.out.println(table.getHeadings());
//        System.out.println(table.getRowsWithColumns());
//        System.out.println(table.getRowsWithColumnsByHeading());
        System.out.println(table.getValueFromCell(2, 3));
        System.out.println(table.getValueFromCell(4, 1));
        System.out.println(table.getValueFromCell(4, "Company"));
        System.out.println(table.getValueFromCell(1, "Country"));


//        driver.quit();
    }

    public void start6CheckBox2DontWork() {
        String lesson = "https://www.youtube.com/watch?v=r3En-cBSgG0&list=PL3U4JD8S3lin3seHzfHlInqMCgzC_1gFW&index=31";
        WebDriver driver = new ChromeDriver();
        driver.get("https://genius.courses/%D0%BA%D0%B0%D0%BA-%D1%81%D1%82%D0%B8%D0%BB%D0%B8%D0%B7%D0%BE%D0%B2%D0%B0%D1%82%D1%8C-checkbox-%D0%B8-radio/");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        final By checkbuttonApple = By.xpath(
                "//*[@id=\"second_solution\"]");
        ParseUtil.applyRandomDelay(10, 15, true);
        driver.findElement(checkbuttonApple).click();

//        System.out.println(checkbox.getText());
//        final By checkbuttonMsi = By.xpath("//label[.//div[normalize-space(text())='MSI']]//div[@class='checkbox__checkmark']");
//        driver.findElement(checkbuttonApple).click();


//        driver.quit();
    }

    public void start6CheckBox1() {
        WebDriver driver = new ChromeDriver();
        driver.get("https://hotline.ua/ua/sr/?q=%D0%BD%D0%BE%D1%83%D1%82%D0%B1%D1%83%D0%BA");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

//        final By checkbuttonApple = By.xpath("//label[.//div[normalize-space(text())='Apple']]//div[@class='checkbox__checkmark']");
        final By checkbuttonApple = By.xpath(
                "/html/body/div/div/div/div[1]/div[3]/div[2]/div[2]/div[1]/div[2]/div[2]/label[7]/div[1]");
        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(checkbuttonApple));
        checkbox.click();

//        final By checkbuttonMsi = By.xpath("//label[.//div[normalize-space(text())='MSI']]//div[@class='checkbox__checkmark']");
//        driver.findElement(checkbuttonApple).click();


//        driver.quit();
    }


    public void start5_1() {
        WebDriver driver = new FirefoxDriver();
        driver.get("https://en.wikipedia.org/");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        driver.findElement(By.id("searchInput")).sendKeys("java");
        ParseUtil.applyRandomDelay(1, 2, true);
        driver.findElement(By.cssSelector("button.cdx-button.cdx-search-input__end-button")).click();
        ParseUtil.applyRandomDelay(1, 2, true);
        driver.navigate().back();
        ParseUtil.applyRandomDelay(1, 2, true);
        driver.findElement(By.id("searchInput")).clear();

        String text1 = driver.findElement(By.cssSelector("#mp-itn > ul:nth-child(3) > li:nth-child(2)")).getText();
        System.out.println(text1);


        System.out.println("-------");
        final WebElement element1 = driver.findElement(
                new By.ByXPath("/html/body/div[2]/div/div[3]/main/div[3]/div[3]/div[1]/div[2]/div[2]/div[1]/ul/li[2]/b/a"));
        System.out.println("text " + element1.getText());
        System.out.println("href " + element1.getAttribute("href"));
        System.out.println("title " + element1.getAttribute("title"));
//        driver.quit();
    }


    public void start5sendKeys() {
        WebDriver driver = new FirefoxDriver();
        driver.get("https://en.wikipedia.org/");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        final By xpathSearchButton = By.xpath("//*[@id=\"searchInput\"]");
        final By idSearchButton = By.id("searchInput");
        final By searchButton = By.xpath("/html/body/div[1]/header/div[2]/div/div/div/form/button");
        final WebElement element = driver.findElement(xpathSearchButton);
        element.sendKeys("religion");
        driver.findElement(searchButton).click();
//        System.out.println(element.getText());

        driver.findElement(xpathSearchButton).sendKeys("religion");


        element.click();
        element.submit();

//        driver.quit();
    }


    //    @PostConstruct
    public void start4click_submit() {
        WebDriver driver = new FirefoxDriver();
        driver.get("https://en.wikipedia.org/");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        final By xpathSearchButton = By.xpath("button.cdx-button.cdx-search-input__end-button");
        final WebElement element = driver.findElement(xpathSearchButton);
        System.out.println(element.getText());
        element.click();
        element.submit();

        driver.quit();
    }

    public void start3CssSelector() {
        WebDriver driver = new FirefoxDriver();
        driver.get("https://en.wikipedia.org/");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        By.xpath("//*[@id=\"ca-view\"]/a/span");
        By.linkText("In the news");
        By.cssSelector("div#simpleSearch input#searchInput"); //id
        By.cssSelector("div.cdx-text-input"); // class
        By.cssSelector("div.cdx-typeahead-search > form.cdx-search-input--has-end-button > div"); // class
        By.id("simpleSearch");
        By.className("cdx-text-input");
        By.name("search");
        By.partialLinkText("Community port");
        By.tagName("div");

        final WebElement elementByTag = driver.findElement(By.tagName("div"));

        driver.quit();
    }

    public void implicitlyWait() {
        WebDriver driver = new FirefoxDriver();
//        driver.manage().window().maximize();
        driver.manage().window().setSize(new Dimension(900, 500));
        driver.get("https://www.google.com/");
        driver.navigate().to("https://www.google.com/");
        driver.navigate().back();
        driver.navigate().forward();
        driver.navigate().refresh();

        driver.quit();

    }

    public void startFindLast() {
        WebDriver driver = new ChromeDriver();
        driver.get("https://cpu.userbenchmark.com/");
        String a1 = "//table[@id='customers']//tr[last()]";

        driver.quit();

    }

    private void functionXPath() {
        WebDriver driver = new ChromeDriver();
        String w3schools_html_tables1 = "https://www.w3schools.com/html/html_tables.asp";

        driver.get(w3schools_html_tables1);
        driver.findElement(By.xpath(
                "span[@class='mw-page-title-main' and text()='Список учебных заведений Таравы']"));

        driver.quit();
    }

    private void links() {

        String linkWiki = "https://ru.wikipedia.org/wiki/";

        String pathFul = "/html/body/div/div/a";
        String pathShort = "//body //a";  //  //*[@id="top"]
        String pathShort2 = "//input[@id='searchInput']";
        String pathShort3 = "//*[@id=\"searchInput\"]";
        String pathShort4 = "//a[@class='mw-wiki-logo']";
        String pathShort6 = "//div[@class='main-block main-box' and @id='main-tfl']//link[1]";
        String pathShort7 = "//*[@id='main-tfl']";

        String w3schools_html_tables1 = "https://www.w3schools.com/html/html_tables.asp";


        //*[@id="main-dyk"]

    }
}
