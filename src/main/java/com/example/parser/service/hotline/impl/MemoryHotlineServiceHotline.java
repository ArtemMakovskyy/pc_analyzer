package com.example.parser.service.hotline.impl;

import com.example.parser.dto.hotline.MemoryHotLineParserDto;
import com.example.parser.dto.mapper.MemoryHotLineMapper;
import com.example.parser.exсeption.CustomServiceException;
import com.example.parser.model.hotline.MemoryHotLine;
import com.example.parser.repository.MemoryHotLineRepository;
import com.example.parser.service.hotline.HotlineDataUpdateService;
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
public class MemoryHotlineServiceHotline implements HotlineDataUpdateService {
    private static final String MEMORY_PAGE_LINK_DDR4 =
            "https://hotline.ua/ua/computer/moduli-pamyati-dlya-pk-i-noutbukov"
                    + "/3102-19139-98765/?sort=priceUp";
    private static final String MEMORY_PAGE_LINK_DDR5 =
            "https://hotline.ua/ua/computer/moduli-pamyati-dlya-pk-i-noutbukov"
                    + "/3102-19139-672508/?sort=priceUp";

    private final MultiThreadPagesParser<MemoryHotLineParserDto> memoryPageParserImpl;
    private final MemoryHotLineRepository memoryHotLineRepository;
    private final MemoryHotLineMapper memoryHotLineMapper;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public void refreshDatabaseWithParsedData(ExecutorService executor) {
        try {
            log.info("Starting memory data update process...");
            List<MemoryHotLineParserDto> memories = memoryPageParserImpl
                    .parsePage(MEMORY_PAGE_LINK_DDR4);
            memories.addAll(memoryPageParserImpl
                    .parsePage(MEMORY_PAGE_LINK_DDR5));

            log.info("Parsed {} memories.", memories.size());
            memoryHotLineRepository.deleteAll();
            log.info("Deleted old memory data.");

            List<MemoryHotLine> memoryHotLineList = memories.stream()
                    .map(memoryHotLineMapper::toEntity)
                    .toList();

            List<MemoryHotLine> memoryHotLinesFromDb =
                    memoryHotLineRepository.saveAll(memoryHotLineList);
            log.info("Saved {} new memory records.", memoryHotLinesFromDb.size());
        } catch (Exception e) {
            log.error("Error occurred during memory data update process: {}",
                    e.getMessage(), e);
            throw new CustomServiceException("Failed to process memory data", e);
        }
    }

}
