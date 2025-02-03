package com.example.parser.dto.mapper;

import com.example.parser.config.MapperConfig;
import com.example.parser.dto.hotline.GpuHotLineParserDto;
import com.example.parser.dto.hotline.MemoryHotLineParserDto;
import com.example.parser.model.hotline.GpuHotLine;
import com.example.parser.model.hotline.MemoryHotLine;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(config = MapperConfig.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = GpuUserBenchmarkMapper.class)
public interface GpuHotLineMapper {
    GpuHotLine toEntity(GpuHotLineParserDto dto);
}
