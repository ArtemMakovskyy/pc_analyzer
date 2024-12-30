package com.example.parser.model.hotline;

import lombok.Data;

@Data
public class GpuHotLineCreateDto {
    private String totalName;
    private String name;
    private String memoryType;
    private String memorySize;
    private String shina;
    private String port;
    private String year;
    private String price;
    private Double avgBenchUserBenchmark;
}
