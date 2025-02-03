package com.example.parser.dto.mapper;

import com.example.parser.config.MapperConfig;
import com.example.parser.dto.hotline.MemoryHotLineParserDto;
import com.example.parser.dto.hotline.MotherBoardHotLineParserDto;
import com.example.parser.model.hotline.MemoryHotLine;
import com.example.parser.model.hotline.MotherBoardHotLine;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(config = MapperConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MotherBoardHotLineMapper {
    MotherBoardHotLine toEntity(MotherBoardHotLineParserDto dto);
}
