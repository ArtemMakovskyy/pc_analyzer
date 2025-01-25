package com.example.parser.service.parse;

import java.util.List;

public interface PageParser<T> {
    List<T> parsePage(String url);
}
