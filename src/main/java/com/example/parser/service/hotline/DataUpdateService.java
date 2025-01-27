package com.example.parser.service.hotline;

import java.util.concurrent.ExecutorService;

public interface DataUpdateService {
    void refreshDatabaseWithParsedData(ExecutorService executor);
}