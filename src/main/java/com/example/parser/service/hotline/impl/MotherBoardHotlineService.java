package com.example.parser.service.hotline.impl;

import com.example.parser.exseption.CustomServiceException;
import com.example.parser.model.hotline.MotherBoardHotLine;
import com.example.parser.repository.MotherBoardHotLineRepository;
import com.example.parser.service.hotline.DataUpdateService;
import com.example.parser.service.parse.MultiThreadPagesParser;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class MotherBoardHotlineService implements DataUpdateService {
    private final MultiThreadPagesParser<MotherBoardHotLine> motherBoardPageParserImpl;
    private final MotherBoardHotLineRepository motherBoardHotLineRepository;

//    @PostConstruct
//    public void init(){
//            refreshDatabaseWithParsedData();
//    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public void refreshDatabaseWithParsedData() {
        try {
            log.info("Starting motherboard data update process...");
            List<MotherBoardHotLine> motherBoards
                    = motherBoardPageParserImpl.parseAllMultiThread();

            log.info("Parsed {} motherboards.", motherBoards.size());
            motherBoardHotLineRepository.deleteAll();
            log.info("Deleted old motherboard data.");

            final List<MotherBoardHotLine> motherBoardHotLinesFromDb
                    = motherBoardHotLineRepository.saveAll(motherBoards);
            log.info("Saved {} new motherboard records.",
                    motherBoardHotLinesFromDb.size());
        } catch (Exception e) {
            log.error("Error occurred during motherboard data update process: {}"
                    , e.getMessage(), e);
            throw new CustomServiceException("Failed to process motherboard data", e);
        }
    }

}
