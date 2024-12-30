package com.example.parser.model.hotline;

import lombok.Data;

@Data
public class CpuHotLineCreateDto {
    private String name;
    private String url;
    private String prices;

    private String socketType;
    private String frequency;
    private String l3Cache;
    private String cores;
    private String threads;
    private String packageType;
    private String releaseDate;


    private String brand;
    private String type;
    private double baseFrequency;
    private double maxFrequency;
    private String coreName;
    private int coreCount;
    private int threadCount;

    private Double avgBenchUserBenchmark;
}
