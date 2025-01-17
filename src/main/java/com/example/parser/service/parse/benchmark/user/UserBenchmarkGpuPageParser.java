package com.example.parser.service.parse.benchmark.user;

import com.example.parser.model.user.benchmark.UserBenchmarkGpu;
import com.example.parser.utils.ParseUtil;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserBenchmarkGpuPageParser {
    private final UserBenchmarkTestPage userBenchmarkTestPage;
    private static final ParseUtil.DelayInSeconds SMALL_PAUSE
            = new ParseUtil.DelayInSeconds(2, 4);
    private static final ParseUtil.DelayInSeconds BIG_PAUSE
            = new ParseUtil.DelayInSeconds(4, 10);
    private static final String BASE_URL = "https://gpu.userbenchmark.com/";
    private static final String XPATH_BUTTON_PRICE_SORT
            = "//*[@id=\"tableDataForm:mhtddyntac\"]/table/thead/tr//th[@data-mhth='MC_PRICE'][1]";
    private static final String XPATH_LOCATOR_PAGE_QUANTITY
            = "//*[@id='tableDataForm:mhtddyntac']/nav/ul/li[1]/a";
    private static final String XPATH_NEXT_PAGE_BUTTON
            = "//*[@id=\"tableDataForm:j_idt260\"]";

    private static final String CSS_QUERY_TABLE_ROW = "tr.hovertarget";

    private static final String PAGE_QUANTITY_PATTERN = "Page \\d+ of (\\d+)";


    public List<UserBenchmarkGpu> purse() {
        WebDriver driver = new ChromeDriver();
        try {
            driver.get(BASE_URL);
            userBenchmarkTestPage.checkAndPassTestIfNecessary(driver);
            int pages = findPageQuantity(driver);
            sortByPriceButton(driver);
            return parsePages(driver, pages);
        } finally {
            driver.quit();
        }
    }

    private List<UserBenchmarkGpu> parsePages(WebDriver driver, int pages) {

        log.info("Start open pages. Total quality is: " + pages);
        List<UserBenchmarkGpu> gpuUserBenchmarks = new ArrayList<>();
        int currentPage = 1;
        do {
            log.info("Current page is " + currentPage + " from " + pages + ".");
            final List<UserBenchmarkGpu> gpusUserBenchmarksOnPage
                    = parsePage(driver);
            gpuUserBenchmarks.addAll(gpusUserBenchmarksOnPage);
            log.info("Pause 3 in parsePages() before click on next page");
            ParseUtil.applyRandomDelay(BIG_PAUSE);
            currentPage++;
            if (currentPage < pages) {
                clickNextPage(driver);
            }
        } while (currentPage < pages);
        log.info("Parse pages successfully stopped");
        return gpuUserBenchmarks;
    }

    private List<UserBenchmarkGpu> parsePage(WebDriver driver) {
        String currentHtmlPageSource = driver.getPageSource();
        List<UserBenchmarkGpu> gpuUserBenchmarksOnPage
                = pursePageSource(currentHtmlPageSource);

        if (false) {

            gpuUserBenchmarksOnPage.forEach(System.out::println);
            System.out.println(gpuUserBenchmarksOnPage.size());
        }
        log.info("Pause 2 in parsePage()");
        ParseUtil.applyRandomDelay(BIG_PAUSE);

        return gpuUserBenchmarksOnPage;
    }

    private List<UserBenchmarkGpu> pursePageSource(String pageSource) {
        Document htmlDocument = Jsoup.parse(pageSource);

        /**
         * <tr class="hovertarget " data-id="1943305">
         */

        Elements rows = htmlDocument.select(CSS_QUERY_TABLE_ROW);
        UserBenchmarkGpu item;
        List<UserBenchmarkGpu> items = new ArrayList<>();
        for (Element row : rows) {
            item = rowToGpu(row);
            items.add(item);
        }
        return items;
    }

    private UserBenchmarkGpu rowToGpu(Element row) {
        //todo fix it
        //            String column1NumberPosition = row.select("td:nth-child(1) div")
//                    .text();
        String column2_1Manufacturer = row.select("td:nth-child(2) span.semi-strongs")
                .first().ownText().trim();
        String column2_2Model = row.select("td:nth-child(2) span.semi-strongs a.nodec")
                .text().trim();
        String column3UserRating = row.select("td:nth-child(3) div.mh-tc")
                .first().text().replaceAll("[^0-9]", "");
        String column4Value = row.select("td:nth-child(4) div.mh-tc")
                .text().trim();
        String column5_1Avg = row.select("td:nth-child(5) div.mh-tc")
                .text().split(" ")[0];
//            String column5_2AvgFromTo = row.select("td:nth-child(5) div.mh-tc-cap")
//                    .text();
        String column6Memory = row.select("td:nth-child(6) div.mh-tc")
                .text();
        String column7Core = row.select("td:nth-child(7) div.mh-tc")
                .text();
        String column8Price = row.select("td:nth-child(8) div.mh-tc")
                .text().replaceAll("[^0-9]", "");
        String column9Age = row.select("td:nth-child(9) div.mh-tc")
                .text();
        String column10Price = row.select("td:nth-child(10) div.mh-tc")
                .text().replaceAll("[^0-9]", "");

        if (false) {
//                System.out.println("Column 1: " + column1NumberPosition);
            System.out.println("Column 201: " + column2_1Manufacturer);
            System.out.println("Column 202: " + column2_2Model);
            System.out.println("Column 3: " + column3UserRating);
            System.out.println("Column 4: " + column4Value);
            System.out.println("Column 5: " + column5_1Avg);
//                System.out.println("Column 5: " + column5_2AvgFromTo);
            System.out.println("Column 6: " + column6Memory);
            System.out.println("Column 7: " + column7Core);
            System.out.println("Column 8: " + column8Price);
            System.out.println("Column 9: " + column9Age);
            System.out.println("Column 10: " + column10Price);
            System.out.println("------------------------");
        }
        UserBenchmarkGpu item = new UserBenchmarkGpu();
        item.setModel(column2_2Model);
        //todo check if this work
        item.setModelHl(formatHlGpuName(column2_2Model));
        item.setManufacturer(column2_1Manufacturer);
        item.setUserRating(ParseUtil.stringToDouble(column3UserRating));
        item.setValuePercents(ParseUtil.stringToDouble(column4Value));
        item.setAvgBench(ParseUtil.stringToDouble(column5_1Avg));
        item.setPrice(ParseUtil.stringToDouble(column8Price));
        item.setUrlOfGpu(row.select("td a.nodec").attr("href"));
        return item;
    }

    private static String formatHlGpuName(String input) {
        if (input.contains("-TS (Ti-Super)")) {
            return input.replace("-TS (Ti-Super)", " Ti SUPER");
        } else if (input.contains("-S (Super)")) {
            return input.replace("-S (Super)", " SUPER");
        } else if (input.contains("S (Super)")) {
            return input.replace("S (Super)", " SUPER");
        } else if (input.contains("-Ti")) {
            return input.replace("-Ti", " Ti");
        }else if (input.contains("-XT")) {
            return input.replace("-XT", " XT");
        }
        return input;
    }

    private int findPageQuantity(WebDriver driver) {
        int pages;

        By pageInfoLocator = By.xpath(XPATH_LOCATOR_PAGE_QUANTITY);

        WebDriverWait wait2
                = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement pageInfoElement
                = wait2.until(ExpectedConditions.visibilityOfElementLocated(pageInfoLocator));

        String pageInfoText = pageInfoElement.getText();

        Pattern pattern = Pattern.compile(PAGE_QUANTITY_PATTERN);
        Matcher matcher = pattern.matcher(pageInfoText);

        if (matcher.find()) {
            pages = Integer.parseInt(matcher.group(1));
        } else {
            throw new RuntimeException(
                    "Unable to extract the maximum number of pages from text: "
                            + pageInfoText);
        }

        return pages;
    }

    private void sortByPriceButton(WebDriver driver) {
        By xpathPriceSortButton = By.xpath(XPATH_BUTTON_PRICE_SORT);
        final WebElement elementPriceButton = driver.findElement(xpathPriceSortButton);
        if (!elementPriceButton.isDisplayed()) {
            throw new RuntimeException("Price sort button is not visible.");
        }
        log.info("Pause 1, before click on price button");
        ParseUtil.applyRandomDelay(SMALL_PAUSE);
        elementPriceButton.click();
    }

    private void clickNextPage(WebDriver driver) {
        By xpathNextButton = By.xpath(XPATH_NEXT_PAGE_BUTTON);
        WebElement elementNextButton = driver.findElement(xpathNextButton);
        log.info("Pause 4 in clickNextPage click before click on next page");
        ParseUtil.applyRandomDelay(SMALL_PAUSE);
        elementNextButton.click();

        userBenchmarkTestPage.checkAndPassTestIfNecessary(driver);
    }

}
