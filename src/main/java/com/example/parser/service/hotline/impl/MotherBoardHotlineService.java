package com.example.parser.service.hotline.impl;

import com.example.parser.dto.hotline.MotherBoardHotLineParserDto;
import com.example.parser.dto.mapper.MotherBoardHotLineMapper;
import com.example.parser.exсeption.CustomServiceException;
import com.example.parser.model.hotline.MotherBoardHotLine;
import com.example.parser.repository.MotherBoardHotLineRepository;
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
public class MotherBoardHotlineService implements DataUpdateService {
    private final MultiThreadPagesParser<MotherBoardHotLineParserDto> motherBoardPageParserImpl;
    private final MotherBoardHotLineRepository motherBoardHotLineRepository;
    private final MotherBoardHotLineMapper motherBoardHotLineMapper;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public void refreshDatabaseWithParsedData(ExecutorService executor) {
        try {
            log.info("Starting motherboard data update process...");
            List<MotherBoardHotLineParserDto> motherBoards
                    = motherBoardPageParserImpl.parseAllMultiThread(executor);

            log.info("Parsed {} motherboards.", motherBoards.size());
            motherBoardHotLineRepository.deleteAll();
            log.info("Deleted old motherboard data.");

            List<MotherBoardHotLine> mBlist = motherBoards.stream()
                    .map(motherBoardHotLineMapper::toEntity)
                    .toList();

            List<MotherBoardHotLine> motherBoardHotLinesFromDb
                    = motherBoardHotLineRepository.saveAll(mBlist);

            log.info("Saved {} new motherboard records.",
                    motherBoardHotLinesFromDb.size());
        } catch (Exception e) {
            log.error("Error occurred during motherboard data update process: {}"
                    , e.getMessage(), e);
            throw new CustomServiceException("Failed to process motherboard data", e);
        }
    }

}
