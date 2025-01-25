package com.example.parser.service.hotline;

import com.example.parser.exseption.CustomServiceException;
import com.example.parser.model.hotline.MemoryHotLine;
import com.example.parser.repository.MemoryHotLineRepository;
import com.example.parser.service.parse.MultiThreadPagesParser;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemoryHotlineService {
    private final static String MEMORY_PAGE_LINK_DDR4
            = "https://hotline.ua/ua/computer/moduli-pamyati-dlya-pk-i-noutbukov/3102-19139-98765/?sort=priceUp";
    private final static String MEMORY_PAGE_LINK_DDR5 =
            "https://hotline.ua/ua/computer/moduli-pamyati-dlya-pk-i-noutbukov/3102-19139-672508/?sort=priceUp";
    private final MultiThreadPagesParser<MemoryHotLine> memoryPageParserImpl;
    private final MemoryHotLineRepository memoryHotLineRepository;

    @Transactional
    public List<MemoryHotLine> parseThenCleanDbThenSaveNewItems() {
        try {
            log.info("Starting memory data update process...");
            List<MemoryHotLine> memories = memoryPageParserImpl.parsePage(MEMORY_PAGE_LINK_DDR4);
            memories.addAll(memoryPageParserImpl.parsePage(MEMORY_PAGE_LINK_DDR5));

            log.info("Parsed {} memories.", memories.size());
            memoryHotLineRepository.deleteAll();
            log.info("Deleted old memory data.");

            final List<MemoryHotLine> memoryHotLinesFromDb = memoryHotLineRepository.saveAll(memories);
            log.info("Saved {} new memory records.", memoryHotLinesFromDb.size());

            return memories;
        } catch (Exception e) {
            log.error("Error occurred during memory data update process: {}", e.getMessage(), e);
            throw new CustomServiceException("Failed to process memory data", e);
        }
    }

}
