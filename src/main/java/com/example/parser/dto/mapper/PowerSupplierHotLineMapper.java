package com.example.parser.dto.mapper;

import com.example.parser.config.MapperConfig;
import com.example.parser.dto.hotline.MotherBoardHotLineParserDto;
import com.example.parser.dto.hotline.PowerSupplierHotLineParserDto;
import com.example.parser.model.hotline.MotherBoardHotLine;
import com.example.parser.model.hotline.PowerSupplierHotLine;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(config = MapperConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PowerSupplierHotLineMapper {
    PowerSupplierHotLine toEntity(PowerSupplierHotLineParserDto dto);
}
