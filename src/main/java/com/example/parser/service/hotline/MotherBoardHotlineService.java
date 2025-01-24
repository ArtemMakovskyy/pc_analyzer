package com.example.parser.service.hotline;

import com.example.parser.exseption.CustomServiceException;
import com.example.parser.model.hotline.MotherBoardHotLine;
import com.example.parser.repository.MotherBoardHotLineRepository;
import com.example.parser.service.parse.MultiThreadPageParser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class MotherBoardHotlineService {
    private final MultiThreadPageParser<MotherBoardHotLine> motherBoardPageParserImpl;
    private final MotherBoardHotLineRepository motherBoardHotLineRepository;

    @Transactional
    public List<MotherBoardHotLine> parseThenCleanDbThenSaveNewItems() {
        try {
            log.info("Starting motherboard data update process...");
            List<MotherBoardHotLine> motherBoards = motherBoardPageParserImpl.parseMultiThread();

            log.info("Parsed {} motherboards.", motherBoards.size());
            motherBoardHotLineRepository.deleteAll();
            log.info("Deleted old motherboard data.");

            motherBoardHotLineRepository.saveAll(motherBoards);
            log.info("Saved {} new motherboard records.", motherBoards.size());

            return motherBoards;
        } catch (Exception e) {
            log.error("Error occurred during motherboard data update process: {}", e.getMessage(), e);
            throw new CustomServiceException("Failed to process motherboard data", e);
        }
    }
}