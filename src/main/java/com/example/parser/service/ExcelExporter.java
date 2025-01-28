package com.example.parser.service;

import com.example.parser.model.Pc;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

@Component
public class ExcelExporter {

    public void exportToExcel(List<Pc> pcList, String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("PC List");
            int rowNum = 0;

            Row headerRow = sheet.createRow(rowNum++);
            String[] headers = {
                    "ID", "CPU", "Motherboard", "Memory", "GPU", "SSD", "Power Supplier",
                    "Avg GPU Bench", "Gaming Score", "Prediction GPU FPS FHD", "Price"
            };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(getHeaderStyle(workbook));
            }

            for (Pc pc : pcList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(pc.getId());
                row.createCell(1).setCellValue(pc.getCpu().getName());
                row.createCell(2).setCellValue(pc.getMotherboard().getManufacturer() + " " + pc.getMotherboard().getName());
                row.createCell(3).setCellValue(pc.getMemory().getManufacturer() + " " + pc.getMemory().getName());
                row.createCell(4).setCellValue(pc.getGpu().getManufacturer() + " " + pc.getGpu().getName() + " " + pc.getGpu().getMemorySize());
                row.createCell(5).setCellValue(pc.getSsd().getManufacturer() + " " + pc.getSsd().getName());
                row.createCell(6).setCellValue(pc.getPowerSupplier().getManufacturer() + " " + pc.getPowerSupplier().getName() + " " + pc.getPowerSupplier().getPower() + "W");
                row.createCell(7).setCellValue(pc.getAvgGpuBench() != null ? pc.getAvgGpuBench() : 0);
                row.createCell(8).setCellValue(pc.getGamingScore() != null ? pc.getGamingScore() : 0);
                row.createCell(9).setCellValue(pc.getPredictionGpuFpsFHD() != null ? pc.getPredictionGpuFpsFHD() : 0);
                row.createCell(10).setCellValue(pc.getPrice() != null ? pc.getPrice().doubleValue() : 0);
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
            System.out.println("Файл сохранен: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static CellStyle getHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }

}
