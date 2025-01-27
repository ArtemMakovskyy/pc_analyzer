package com.example.parser.service.hotline.impl;

import com.example.parser.exseption.CustomServiceException;
import com.example.parser.model.hotline.SsdHotLine;
import com.example.parser.repository.SsdHotLineRepository;
import com.example.parser.service.hotline.DataUpdateService;
import com.example.parser.service.parse.MultiThreadPagesParser;
import java.util.List;
import java.util.concurrent.ExecutorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class SsdHotlineService implements DataUpdateService {
    private final static String SSD_PAGE_LINK
            = "https://hotline.ua/ua/computer/diski-ssd/66705-301666-301678-301686-608078-667303/";

    private final MultiThreadPagesParser<SsdHotLine> ssdHotlinePageParserImpl;
    private final SsdHotLineRepository ssdHotLineRepository;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public void refreshDatabaseWithParsedData(ExecutorService executor) {
        try {
            log.info("Starting ssd data update process...");
            List<SsdHotLine> ssds = ssdHotlinePageParserImpl.parsePage(SSD_PAGE_LINK);

            log.info("Parsed {} ssd.", ssds.size());
            ssdHotLineRepository.deleteAll();
            log.info("Deleted old ssd data.");

            final List<SsdHotLine> ssdHotLinesFromDb = ssdHotLineRepository.saveAll(ssds);
            log.info("Saved {} new ssd records.", ssdHotLinesFromDb.size());

        } catch (Exception e) {
            log.error("Error occurred during ssd data update process: {}", e.getMessage(), e);
            throw new CustomServiceException("Failed to process ssd data", e);
        }
    }

}
