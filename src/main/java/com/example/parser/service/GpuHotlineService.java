package com.example.parser.service;

import com.example.parser.model.hotline.GpuHotLine;
import com.example.parser.repository.GpuHotLineRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class GpuHotlineService {
    private final GpuHotLineRepository gpuHotLineRepository;

    //todo fixed to dto
    public void saveAll(List<GpuHotLine> createDto){

        gpuHotLineRepository.saveAll(createDto);

    }
}
