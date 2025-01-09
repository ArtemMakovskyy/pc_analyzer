package com.example.parser.service.parse.benchmark.user;

import com.example.parser.model.user.benchmark.CpuUserBenchmark;
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
public class UserBenchmarkCpuPageParser2 {
    private static final String CSS_QUERY_TABLE_ROW = "tr.hovertarget";
    private static final ParseUtil.DelayInSeconds SMALL_PAUSE = new ParseUtil.DelayInSeconds(2, 4);
    private static final ParseUtil.DelayInSeconds BIG_PAUSE = new ParseUtil.DelayInSeconds(4, 10);
    private static final String XPATH_NEXT_PAGE_BUTTON = "//*[@id=\"tableDataForm:j_idt260\"]";
    private static final String XPATH_LOCATOR_PAGE_QUANTITY = "//*[@id='tableDataForm:mhtddyntac']/nav/ul/li[1]/a";
    private static final String PAGE_QUANTITY_PATTERN = "Page \\d+ of (\\d+)";
    private static final String XPATH_BUTTON_PRICE_SORT = "//*[@id=\"tableDataForm:mhtddyntac\"]/table/thead/tr//th[@data-mhth='MC_PRICE'][1]";
    private static final String BASE_URL = "https://cpu.userbenchmark.com/";
    private final UserBenchmarkTestPage userBenchmarkTestPage;

//    @PostConstruct
    public void init() {
        purse().forEach(System.out::println);
    }

    public List<CpuUserBenchmark> purse() {

        WebDriver driver = new ChromeDriver();

        try {
            driver.get(BASE_URL);
            userBenchmarkTestPage.checkAndPassTestIfItNeed(driver);
            int pages = findPageQuantity(driver);
            sortByPriceButton(driver);
//            return parsePages(driver, pages);
            return parsePages(driver, 3);
        } finally {
            driver.quit();
        }
    }

    private List<CpuUserBenchmark> parsePages(WebDriver driver, int pages) {

        log.info("Start open pages. Total quality is: " + pages);
        List<CpuUserBenchmark> cpuUserBenchmarks = new ArrayList<>();
        int currentPage = 1;
        do {
            log.info("Current page is " + currentPage + " from " + pages + ".");
            final List<CpuUserBenchmark> cpusUserBenchmarksOnPage = parsePage(driver);
            cpuUserBenchmarks.addAll(cpusUserBenchmarksOnPage);
            log.info("Pause 3 in parsePages() before click on next page");
            ParseUtil.addRandomDelayInSeconds(BIG_PAUSE);
            currentPage++;
            if (currentPage < pages) {
                clickNextPage(driver);
            }
        } while (currentPage < pages);
        log.info("Parse pages successfully stopped");
        return cpuUserBenchmarks;
    }

    private List<CpuUserBenchmark> parsePage(WebDriver driver) {
        String currentHtmlPageSource = driver.getPageSource();
        List<CpuUserBenchmark> gpuUserBenchmarksOnPage = pursePageSource(currentHtmlPageSource);

        if (false) {

            gpuUserBenchmarksOnPage.forEach(System.out::println);
            System.out.println(gpuUserBenchmarksOnPage.size());
        }
        log.info("Pause 2 in parsePage()");
        ParseUtil.addRandomDelayInSeconds(BIG_PAUSE);

        return gpuUserBenchmarksOnPage;
    }

    private List<CpuUserBenchmark> pursePageSource(String pageSource) {
        Document htmlDocument = Jsoup.parse(pageSource);

        Elements rows = htmlDocument.select(CSS_QUERY_TABLE_ROW);
        CpuUserBenchmark item;
        List<CpuUserBenchmark> items = new ArrayList<>();
        for (Element row : rows) {
            item = rowToCpu(row);
            items.add(item);
        }
        return items;
        //            cpuUserBenchmarkParserByPosition.purseInnerPage(cpu);
    }


    private CpuUserBenchmark rowToCpu(Element row) {

        //            String column1NumberPosition = row.select("td:nth-child(1) div")
//                    .text();
        String column2_1Manufacturer = row.select("td:nth-child(2) span.semi-strongs").first().ownText().trim();
        String column2_2Model = row.select("td:nth-child(2) span.semi-strongs a.nodec").text().trim();
        String column3UserRating = row.select("td:nth-child(3) div.mh-tc").first().text().replaceAll("[^0-9]", "");
        String column4Value = row.select("td:nth-child(4) div.mh-tc").text();
        String column5_1Avg = row.select("td:nth-child(5) div.mh-tc").text().split(" ")[0];
//            String column5_2AvgFromTo = row.select("td:nth-child(5) div.mh-tc-cap")
//                    .text();
        String column6Memory = row.select("td:nth-child(6) div.mh-tc").text();
//            String column7Core = row.select("td:nth-child(7) div.mh-tc")
//                    .text();
//            String column8Mkt = row.select("td:nth-child(8) div.mh-tc")
//                    .text();
//            String column9Age = row.select("td:nth-child(9) div.mh-tc")
//                    .text();
        String column10Price = row.select("td:nth-child(10) div.mh-tc").text().replaceAll("[^0-9]", "");

//            if (false) {
//                System.out.println("Column 1: " + column1NumberPosition);
//                System.out.println("Column 201: " + column2_1Manufacturer);
//                System.out.println("Column 202: " + column2_2Model);
//                System.out.println("Column 3: " + column3UserRating);
//                System.out.println("Column 4: " + column4Value);
//                System.out.println("Column 5: " + column5_1Avg);
//                System.out.println("Column 5: " + column5_2AvgFromTo);
//                System.out.println("Column 6: " + column6Memory);
//                System.out.println("Column 7: " + column7Core);
//                System.out.println("Column 8: " + column8Mkt);
//                System.out.println("Column 9: " + column9Age);
//                System.out.println("Column 10: " + column10Price);
//                System.out.println("------------------------");
//            }
        CpuUserBenchmark cpu = new CpuUserBenchmark();
        cpu.setModel(column2_2Model);
        cpu.setManufacturer(column2_1Manufacturer);
        cpu.setUserRating(ParseUtil.stringToDouble(column3UserRating));
        cpu.setValuePercents(ParseUtil.stringToDouble(column4Value));
        cpu.setAvgBench(ParseUtil.stringToDouble(column5_1Avg));
        cpu.setMemoryPercents(ParseUtil.stringToDouble(column6Memory));
        cpu.setPrice(ParseUtil.stringToDouble(column10Price));
        cpu.setUrlOfCpu(row.select("td a.nodec").attr("href"));
        return cpu;
    }

    private int findPageQuantity(WebDriver driver) {
        int pages;

        By pageInfoLocator = By.xpath(XPATH_LOCATOR_PAGE_QUANTITY);

        WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement pageInfoElement = wait2.until(ExpectedConditions.visibilityOfElementLocated(pageInfoLocator));

        String pageInfoText = pageInfoElement.getText();

        Pattern pattern = Pattern.compile(PAGE_QUANTITY_PATTERN);
        Matcher matcher = pattern.matcher(pageInfoText);

        if (matcher.find()) {
            pages = Integer.parseInt(matcher.group(1));
        } else {
            throw new RuntimeException("Unable to extract the maximum number of pages from text: " + pageInfoText);
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
        ParseUtil.addRandomDelayInSeconds(SMALL_PAUSE);
        elementPriceButton.click();
    }

    private void clickNextPage(WebDriver driver) {
        By xpathNextButton = By.xpath(XPATH_NEXT_PAGE_BUTTON);
        WebElement elementNextButton = driver.findElement(xpathNextButton);
        log.info("Pause 4 in clickNextPage click before click on next page");
        ParseUtil.addRandomDelayInSeconds(SMALL_PAUSE);
        elementNextButton.click();

        userBenchmarkTestPage.checkAndPassTestIfItNeed(driver);
    }

}
