package com.example.parser.model.user.benchmark;

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
