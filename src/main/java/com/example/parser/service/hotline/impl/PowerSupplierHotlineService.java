package com.example.parser.service.hotline.impl;

import com.example.parser.dto.hotline.PowerSupplierHotLineParserDto;
import com.example.parser.dto.mapper.PowerSupplierHotLineMapper;
import com.example.parser.exсeption.CustomServiceException;
import com.example.parser.model.hotline.PowerSupplierHotLine;
import com.example.parser.repository.PowerSupplierHotLineRepository;
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
public class PowerSupplierHotlineService implements DataUpdateService {
    private final MultiThreadPagesParser<PowerSupplierHotLineParserDto> powerSupplierMultiThreadPagesParser;
    private final PowerSupplierHotLineRepository powerSupplierHotLineRepository;
    private final PowerSupplierHotLineMapper powerSupplierHotLineMapper;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public void refreshDatabaseWithParsedData(ExecutorService executor) {
        try {
            log.info("Starting power supplier data update process...");
            List<PowerSupplierHotLineParserDto> items = powerSupplierMultiThreadPagesParser.parseAllMultiThread(executor);

            log.info("Parsed {} power supplier.", items.size());
            powerSupplierHotLineRepository.deleteAll();
            log.info("Deleted old power supplier data.");

            final List<PowerSupplierHotLine> powerSuppliersList = items.stream()
                    .map(powerSupplierHotLineMapper::toEntity)
                    .toList();

            List<PowerSupplierHotLine> powerSupplierHotLinesFromDb
                    = powerSupplierHotLineRepository.saveAll(powerSuppliersList);
            log.info("Saved {} new power supplier records.", powerSupplierHotLinesFromDb.size());
        } catch (Exception e) {
            log.error("Error occurred during power supplier data update process: {}", e.getMessage(), e);
            throw new CustomServiceException("Failed to process power supplier data", e);
        }
    }

}
