package com.example.parser.service.parse;

import java.util.List;

public interface PagesParser<T> extends PageParser<T> {
    List<T> parseAll();
}
