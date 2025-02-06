package com.example.parser.service.hotline;

import java.util.concurrent.ExecutorService;

public interface HotlineDataUpdateService {
    void refreshDatabaseWithParsedData(ExecutorService executor);
}