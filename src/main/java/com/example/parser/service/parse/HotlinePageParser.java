package com.example.parser.service.parse;

import com.example.parser.model.hotline.CpuHotLine;
import java.util.List;
import java.util.concurrent.ExecutorService;

public interface HotlinePageParser<T> {
    List<T> parseAllPagesMultiThread(ExecutorService executor);
    List<T> parseAllPages();
}
