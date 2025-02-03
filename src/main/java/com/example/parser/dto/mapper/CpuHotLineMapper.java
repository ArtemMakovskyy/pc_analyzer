package com.example.parser.dto.mapper;

import com.example.parser.config.MapperConfig;
import com.example.parser.dto.hotline.CpuHotLineParserDto;
import com.example.parser.dto.hotline.GpuHotLineParserDto;
import com.example.parser.model.hotline.CpuHotLine;
import com.example.parser.model.hotline.GpuHotLine;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(config = MapperConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = CpuUserBenchmarkMapper.class
)
public interface CpuHotLineMapper {
    CpuHotLine toEntity(CpuHotLineParserDto dto);

    CpuHotLineParserDto toDto(CpuHotLine cpu);
}
