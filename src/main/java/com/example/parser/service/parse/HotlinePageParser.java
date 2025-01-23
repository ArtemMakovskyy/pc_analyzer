package com.example.parser.service.parse;

import java.util.List;
import java.util.concurrent.ExecutorService;

public interface HotlinePageParser<T> {
    List<T> parseAllPagesMultiThread();
    List<T> parseAllPages();
}
