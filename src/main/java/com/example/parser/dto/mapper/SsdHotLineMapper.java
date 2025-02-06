package com.example.parser.dto.mapper;

import com.example.parser.config.MapperConfig;
import com.example.parser.dto.hotline.SsdHotLineParserDto;
import com.example.parser.model.hotline.SsdHotLine;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(config = MapperConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SsdHotLineMapper {
    SsdHotLine toEntity(SsdHotLineParserDto dto);
}
