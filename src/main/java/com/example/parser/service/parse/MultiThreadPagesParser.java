package com.example.parser.service.parse;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public interface MultiThreadPagesParser<T> extends PagesParser<T> {
    List<T> parseAllMultiThread(ExecutorService executor);
}