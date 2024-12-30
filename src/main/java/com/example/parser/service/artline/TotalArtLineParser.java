package com.example.parser.service.artline;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TotalArtLineParser {
    private final ArtLineParser artLineParser;
    private static final String MAIN_LINK_FIRST_PART = "https://artline.ua/catalog/";
    private static final String MAIN_LINK_FINAL_PART = "/page=";

    public void parse() {
        long startTime = System.nanoTime();
        final List<ArtLinePart> cpus = artLineParser.parse(
                false,
                "CPU",
                MAIN_LINK_FIRST_PART + "protsessory" + MAIN_LINK_FINAL_PART
        );

        final List<ArtLinePart> gpus = artLineParser.parse(
                false,
                "GPU",
                MAIN_LINK_FIRST_PART + "videokarty" + MAIN_LINK_FINAL_PART
        );

        final List<ArtLinePart> ssd = artLineParser.parse(
                false,
                "SSD",
                MAIN_LINK_FIRST_PART + "ssd-nakopiteli" + MAIN_LINK_FINAL_PART
        );

        final List<ArtLinePart> mb = artLineParser.parse(
                false,
                "MB",
                MAIN_LINK_FIRST_PART + "materinskie-platy" + MAIN_LINK_FINAL_PART
        );

        final List<ArtLinePart> memory = artLineParser.parse(
                false,
                "MEMORY",
                MAIN_LINK_FIRST_PART + "operativnaya-pamyat" + MAIN_LINK_FINAL_PART
        );

        final List<ArtLinePart> fun = artLineParser.parse(
                false,
                "FUN",
                MAIN_LINK_FIRST_PART + "sistemy-okhlazhdeniya" + MAIN_LINK_FINAL_PART
        );

        final List<ArtLinePart> pcCase = artLineParser.parse(
                false,
                "CASE",
                MAIN_LINK_FIRST_PART + "korpusa-qube" + MAIN_LINK_FINAL_PART

        );
        final List<ArtLinePart> powerSupply = artLineParser.parse(
                false,
                "POWER SUPPLY",
                MAIN_LINK_FIRST_PART + "bloki-pitaniya" + MAIN_LINK_FINAL_PART
        );
        final List<ArtLinePart> hdd = artLineParser.parse(
                false,
                "HDD",
                MAIN_LINK_FIRST_PART + "hdd-nakopiteli" + MAIN_LINK_FINAL_PART
        );

        List<ArtLinePart> marge = new ArrayList<>();
        marge.addAll(cpus);
        marge.addAll(gpus);
        marge.addAll(ssd);
        marge.addAll(mb);
        marge.addAll(memory);
        marge.addAll(fun);
        marge.addAll(pcCase);
        marge.addAll(powerSupply);
        marge.addAll(hdd);

        long endTime = System.nanoTime();
        long duration = endTime - startTime; // в наносекундах
        System.out.println("Время выполнения: " + duration / 1_000_000 + " миллисекунд");
//        marge.forEach(System.out::println);
    }

    public void parseMultiThreaded() {
        // Начало замера времени
        long startTime = System.currentTimeMillis();

        // Запуск парсинга в отдельных потоках
        CompletableFuture<List<ArtLinePart>> cpusFuture = CompletableFuture.supplyAsync(() ->
                artLineParser.parse(
                        false,
                        "CPU",
                        MAIN_LINK_FIRST_PART + "protsessory" + MAIN_LINK_FINAL_PART
                )
        );

        CompletableFuture<List<ArtLinePart>> gpusFuture = CompletableFuture.supplyAsync(() ->
                artLineParser.parse(
                        false,
                        "GPU",
                        MAIN_LINK_FIRST_PART + "videokarty" + MAIN_LINK_FINAL_PART
                )
        );

        CompletableFuture<List<ArtLinePart>> ssdFuture = CompletableFuture.supplyAsync(() ->
                artLineParser.parse(
                        false,
                        "SSD",
                        MAIN_LINK_FIRST_PART + "ssd-nakopiteli" + MAIN_LINK_FINAL_PART
                )
        );

        CompletableFuture<List<ArtLinePart>> mb = CompletableFuture.supplyAsync(() ->
                artLineParser.parse(
                        false,
                        "MB",
                        MAIN_LINK_FIRST_PART + "materinskie-platy" + MAIN_LINK_FINAL_PART
                )
        );

        CompletableFuture<List<ArtLinePart>> memory = CompletableFuture.supplyAsync(() ->
                artLineParser.parse(
                        false,
                        "MEMORY",
                        MAIN_LINK_FIRST_PART + "operativnaya-pamyat" + MAIN_LINK_FINAL_PART
                )
        );

        CompletableFuture<List<ArtLinePart>> fun = CompletableFuture.supplyAsync(() ->
                artLineParser.parse(
                        false,
                        "FUN",
                        MAIN_LINK_FIRST_PART + "sistemy-okhlazhdeniya" + MAIN_LINK_FINAL_PART
                )
        );

        CompletableFuture<List<ArtLinePart>> pcCase = CompletableFuture.supplyAsync(() ->
                artLineParser.parse(
                        false,
                        "CASE",
                        MAIN_LINK_FIRST_PART + "korpusa-qube" + MAIN_LINK_FINAL_PART
                )
        );

        CompletableFuture<List<ArtLinePart>> powerSupply = CompletableFuture.supplyAsync(() ->
                artLineParser.parse(
                        false,
                        "SSD",
                        MAIN_LINK_FIRST_PART + "bloki-pitaniya" + MAIN_LINK_FINAL_PART
                )
        );

        CompletableFuture<List<ArtLinePart>> hdd = CompletableFuture.supplyAsync(() ->
                artLineParser.parse(
                        false,
                        "HDD",
                        MAIN_LINK_FIRST_PART + "hdd-nakopiteli" + MAIN_LINK_FINAL_PART
                )
        );

        try {
            // Ожидание завершения всех задач и сбор результатов
            List<ArtLinePart> mergedList = CompletableFuture
                    .allOf(cpusFuture, gpusFuture, ssdFuture) // Ожидаем завершения всех
                    .thenApply(v -> {
                        // Объединяем результаты
                        List<ArtLinePart> merged = new ArrayList<>();
                        try {
                            merged.addAll(cpusFuture.get());
                            merged.addAll(gpusFuture.get());
                            merged.addAll(ssdFuture.get());
                            merged.addAll(mb.get());
                            merged.addAll(memory.get());
                            merged.addAll(fun.get());
                            merged.addAll(pcCase.get());
                            merged.addAll(powerSupply.get());
                            merged.addAll(hdd.get());
                        } catch (InterruptedException | ExecutionException e) {
                            throw new RuntimeException(e);
                        }
                        return merged;
                    })
                    .join(); // Блокируемся до завершения

            // Вывод результатов
//            mergedList.forEach(System.out::println);

//            saveCsvFileFromList(mergedList);
            saveXlsFileFromList(mergedList);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Конец замера времени
        long endTime = System.currentTimeMillis();
        System.out.println("Время выполнения: " + (endTime - startTime) + " миллисекунд");
    }

    private void saveCsvFileFromList(List<ArtLinePart> parts) {
        File csvFile = new File("artline_parts.csv");

        try (PrintWriter printWriter = new PrintWriter(csvFile)) {
            // Записываем заголовки
            List<String> headers = new ArrayList<>();
            headers.add("\"part\"");
            headers.add("\"productId\"");
            headers.add("\"productUrl\"");
            headers.add("\"productTitle\"");
            headers.add("\"price\"");
            headers.add("\"availability\"");
            printWriter.println(String.join(",", headers));

            // Записываем данные
            for (ArtLinePart part : parts) {
                List<String> row = new ArrayList<>();
                row.add("\"" + part.getPart() + "\"");
                row.add("\"" + part.getProductId() + "\"");
                row.add("\"" + part.getProductUrl() + "\"");
                row.add("\"" + part.getProductTitle() + "\"");
                row.add("\"" + part.getPrice() + "\"");
                row.add("\"" + part.getAvailability() + "\"");

                printWriter.println(String.join(",", row));
            }
            System.out.println("Данные успешно сохранены в файл: " + csvFile.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("Ошибка при сохранении файла: " + e.getMessage());
        }
    }

    private void saveXlsFileFromList(List<ArtLinePart> parts) {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("ArtLineParts");

        Row headerRow = sheet.createRow(0);
        String[] headers = {"Part", "Product ID", "Product URL", "Product Title", "Price", "Availability"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            // Опционально: добавляем стиль для заголовка
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            cell.setCellStyle(style);
        }

        // Записываем данные
        int rowIndex = 1; // Начинаем с первой строки после заголовка
        for (ArtLinePart part : parts) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(part.getPart());
            row.createCell(1).setCellValue(part.getProductId());
            row.createCell(2).setCellValue(part.getProductUrl());
            row.createCell(3).setCellValue(part.getProductTitle());
            row.createCell(4).setCellValue(part.getPrice());
            row.createCell(5).setCellValue(part.getAvailability());
        }

        // Сохраняем файл
        try (FileOutputStream fileOut = new FileOutputStream("artline_parts.xlsx")) {
            workbook.write(fileOut);
            System.out.println("Файл успешно сохранен: artline_parts.xlsx");
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении файла: " + e.getMessage());
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                System.err.println("Ошибка при закрытии workbook: " + e.getMessage());
            }
        }
    }
}
