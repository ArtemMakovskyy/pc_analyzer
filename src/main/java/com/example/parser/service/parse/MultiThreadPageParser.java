package com.example.parser.service.parse;

import java.util.List;

public interface MultiThreadPageParser<T> extends PageParser<T> {
    List<T> parseMultiThread();
}