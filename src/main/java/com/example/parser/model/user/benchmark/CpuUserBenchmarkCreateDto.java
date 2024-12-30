package com.example.parser.model.user.benchmark;

import lombok.Data;

@Data
public class CpuUserBenchmarkCreateDto {
    private String model;
    private String manufacturer;
    private Double userRating;
    private Double valuePercents;
    private Double avgBench;
    private Double memoryPercents;
    private Double price;
    private String urlOfCpu;
    private String partNumber;
    private String cpuSpecification;
    private Double desktopScore;
    private Double gamingScore;
    private Double workstationScore;
}
