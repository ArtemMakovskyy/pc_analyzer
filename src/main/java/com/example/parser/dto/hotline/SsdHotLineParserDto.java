package com.example.parser.dto.hotline;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
public class SsdHotLineParserDto {
    private String name;
    private String url;
    private String prices;
    private Double avgPrice;
    private Integer propositionsQuantity;
    private String manufacturer;
    private String type;
    private String capacity;
    private String readingSpeed;
    private String writingSpeed;

}
