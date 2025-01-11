package com.example.parser.dto.mapper;

import com.example.parser.config.MapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(config = MapperConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GpuUserBenchmarkMapper {

}
