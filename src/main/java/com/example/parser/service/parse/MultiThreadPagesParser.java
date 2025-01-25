package com.example.parser.service.parse;

import java.util.List;

public interface MultiThreadPagesParser<T> extends PagesParser<T> {
    List<T> parseAllMultiThread();
}