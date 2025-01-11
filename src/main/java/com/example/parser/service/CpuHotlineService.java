package com.example.parser.service;

import com.example.parser.model.hotline.CpuHotLine;
import com.example.parser.repository.CpuHotLineRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class CpuHotlineService {
    private final CpuHotLineRepository cpuHotLineRepository;

    //todo fixed to dto
    public void saveAll(List<CpuHotLine> createDto){

        cpuHotLineRepository.saveAll(createDto);

    }
}
