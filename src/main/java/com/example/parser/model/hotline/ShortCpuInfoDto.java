package com.example.parser.model.hotline;

import lombok.Data;

@Data
public class ShortCpuInfoDto {
    private String socketType;
    private String frequency;
    private String l3Cache;
    private String cores;
    private String threads;
    private String packageType;
    private String releaseDate;
}
