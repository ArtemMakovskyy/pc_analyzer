package com.example.parser.dto.userbenchmark;

import lombok.Data;

@Data
public class GpuUserBenchmarkCreateDto {
    private String model;
    private String manufacturer;
    private Double userRating;
    private Double valuePercents;
    private Double avgBench;
    private Double price;
    private String urlOfCpu;
}
