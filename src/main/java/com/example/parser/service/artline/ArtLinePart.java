package com.example.parser.service.artline;

import lombok.Data;

@Data
public class ArtLinePart {
    private String part;
    private String productId;
    private String productUrl;
    private String productTitle;
    private Double price;
    private Boolean availability;
}
