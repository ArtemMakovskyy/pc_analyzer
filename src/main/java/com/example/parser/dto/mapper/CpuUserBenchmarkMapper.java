package com.example.parser.dto.mapper;

import com.example.parser.config.MapperConfig;
import com.example.parser.dto.userbenchmark.CpuUserBenchmarkParserDto;
import com.example.parser.model.user.benchmark.UserBenchmarkCpu;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(config = MapperConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CpuUserBenchmarkMapper {
    UserBenchmarkCpu toEntity (CpuUserBenchmarkParserDto dto);
    CpuUserBenchmarkParserDto toDto (UserBenchmarkCpu cpu);
}
