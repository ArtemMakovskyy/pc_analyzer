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
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class UserBenchmarkGpuPageParser {

    private static final ParseUtil.DelayInSeconds SMALL_PAUSE =
            new ParseUtil.DelayInSeconds(2, 4);
    private static final ParseUtil.DelayInSeconds BIG_PAUSE =
            new ParseUtil.DelayInSeconds(4, 10);

    private static final String XPATH_BUTTON_PRICE_SORT =
            "//*[@id=\"tableDataForm:mhtddyntac\"]/table/thead/tr//th"
                    + "[@data-mhth='MC_PRICE'][1]";
    private static final String XPATH_LOCATOR_PAGE_QUANTITY =
            "//*[@id='tableDataForm:mhtddyntac']/nav/ul/li[1]/a";
    private static final String XPATH_NEXT_PAGE_BUTTON =
            "//*[@id=\"tableDataForm:j_idt260\"]";

    private static final String BASE_URL = "https://gpu.userbenchmark.com/";
    private static final String PAGE_QUANTITY_PATTERN = "Page \\d+ of (\\d+)";
    private static final String ONLY_DIGITS_PATTERN = "[^0-9]";
    private static final String TABLE_ROW_CSS_SELECTOR = "tr.hovertarget";
    private static final String MANUFACTURER_CSS_SELECTOR =
            "td:nth-child(2) span.semi-strongs";
    private static final String MODEL_CSS_SELECTOR =
            "td:nth-child(2) span.semi-strongs a.nodec";
    private static final String USER_RATING_CSS_SELECTOR =
            "td:nth-child(3) div.mh-tc";
    private static final String VALUE_PERCENTS_CSS_SELECTOR =
            "td:nth-child(4) div.mh-tc";
    private static final String AVG_BENCH_CSS_SELECTOR =
            "td:nth-child(5) div.mh-tc";
    private static final String PRICE_CSS_SELECTOR =
            "td:nth-child(8) div.mh-tc";
    private static final String URL_CSS_SELECTOR = "td a.nodec";


    private final UserBenchmarkTestPage userBenchmarkTestPage;

    public List<UserBenchmarkGpu> parse(int pages) {
        WebDriver driver = new ChromeDriver();
        try {
            driver.get(BASE_URL);
            userBenchmarkTestPage.checkAndPassTestIfNecessary(driver);
            if (pages == 0) {
                pages = findPageQuantity(driver);
            }
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
                = parsePageSource(currentHtmlPageSource);

        log.info("Pause 2 in parsePage()");
        ParseUtil.applyRandomDelay(BIG_PAUSE);
        return gpuUserBenchmarksOnPage;
    }

    private List<UserBenchmarkGpu> parsePageSource(String pageSource) {
        Document htmlDocument = Jsoup.parse(pageSource);
        Elements rows = htmlDocument.select(TABLE_ROW_CSS_SELECTOR);
        UserBenchmarkGpu item;
        List<UserBenchmarkGpu> items = new ArrayList<>();
        for (Element row : rows) {
            item = rowToGpu(row);
            items.add(item);
        }
        return items;
    }

    private UserBenchmarkGpu rowToGpu(Element row) {
        UserBenchmarkGpu item = new UserBenchmarkGpu();
        item.setModel(getElementText(row, MODEL_CSS_SELECTOR));
        item.setModelHl(formatHlGpuName(item.getModel()));
        item.setManufacturer(getElementText(row, MANUFACTURER_CSS_SELECTOR));
        item.setUserRating(ParseUtil.stringToDouble(
                getElementText(row, USER_RATING_CSS_SELECTOR)
                        .replaceAll(ONLY_DIGITS_PATTERN, "")));
        item.setValuePercents(ParseUtil.stringToDouble(
                getElementText(row, VALUE_PERCENTS_CSS_SELECTOR)));
        item.setAvgBench(ParseUtil.stringToDouble(
                getElementText(row, AVG_BENCH_CSS_SELECTOR).split(" ")[0]));
        item.setPrice(ParseUtil.stringToDouble(getElementText(row, PRICE_CSS_SELECTOR)
                .replaceAll(ONLY_DIGITS_PATTERN, "")));
        item.setUrlOfGpu(row.select(URL_CSS_SELECTOR).attr("href"));
        return item;
    }

    private String getElementText(Element row, String selector) {
        Element element = row.select(selector).first();
        return element != null ? element.text().trim() : "";
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
        } else if (input.contains("-XT")) {
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
