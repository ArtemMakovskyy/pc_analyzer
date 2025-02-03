package com.example.parser.dto.mapper;

import com.example.parser.config.MapperConfig;
import com.example.parser.dto.userbenchmark.GpuUserBenchmarkParserDto;
import com.example.parser.model.user.benchmark.UserBenchmarkGpu;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(config = MapperConfig.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface GpuUserBenchmarkMapper {
    UserBenchmarkGpu toEntity(GpuUserBenchmarkParserDto dto);
}
