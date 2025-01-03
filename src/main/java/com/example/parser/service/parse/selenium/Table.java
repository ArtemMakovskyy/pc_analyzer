package com.example.parser.service.parse.selenium;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

@AllArgsConstructor
public class Table {
    private WebElement tableElement;
    private WebDriver driver;

    public List<WebElement> getRows() {
        List<WebElement> rows = tableElement.findElements(By.xpath(".//tr"));
        rows.remove(0);
        return rows;
    }

    public List<WebElement> getHeadings() {
        final WebElement headingRow
                = tableElement.findElement(By.xpath(".//tr[1]"));
        final List<WebElement> headingColumn
                = headingRow.findElements(By.xpath(".//th"));
        return headingColumn;
    }

    public List<List<WebElement>> getRowsWithColumns() {
        final List<WebElement> rows = getRows();
        List<List<WebElement>> rowsWithColumns = new ArrayList<>();

        for (WebElement row : rows) {
            List<WebElement> rowWithColumns = row.findElements(
                    By.xpath(".//td"));
            rowsWithColumns.add(rowWithColumns);
        }
        return rowsWithColumns;
    }

    public List<Map<String, WebElement>> getRowsWithColumnsByHeading() {
        List<List<WebElement>> rowsWithColumns = getRowsWithColumns();
        List<Map<String, WebElement>> rowsWithColumnsByHeading = new ArrayList<>();
        Map<String, WebElement> rowByHeadings;
        List<WebElement> headingsColumns = getHeadings();

        for (List<WebElement> row : rowsWithColumns) {
            rowByHeadings = new HashMap<>();
            for (int i = 0; i < headingsColumns.size(); i++) {
                final String headingText = headingsColumns.get(i).getText();
                final WebElement cell = row.get(i);
                rowByHeadings.put(headingText, cell);
            }
            rowsWithColumnsByHeading.add(rowByHeadings);
        }
        return rowsWithColumnsByHeading;
    }

    public String getValueFromCell(int rowNumber, int columnNumber) {
        final List<List<WebElement>> rowsWithColumns = getRowsWithColumns();
        WebElement cell = rowsWithColumns.get(rowNumber - 1).get(columnNumber - 1);
        return cell.getText();
    }

    public String getValueFromCell(int rowNumber, String columnName) {
        final List<Map<String,WebElement>> rowsWithColumnsByHeadings = getRowsWithColumnsByHeading();
        return rowsWithColumnsByHeadings.get(rowNumber - 1).get(columnName).getText();
    }
}
