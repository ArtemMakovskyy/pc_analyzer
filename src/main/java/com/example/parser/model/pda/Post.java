package com.example.parser.model.pda;

import lombok.Data;

@Data
public class Post {
    private String title;
    private String detailsLink;
    private String author;
    private String authorDetailsLink;
    private String dateOfCreated;
}
