package com.lbm.mapper;

import com.lbm.dto.LocationDto;
import com.lbm.entity.core.LocationData;
import com.lbm.utils.GlobalMapping;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(config = GlobalMapping.class)
public interface LocationMapper {

    LocationMapper INSTANCE = Mappers.getMapper(LocationMapper.class);

    LocationData toEntity(LocationDto dto);

    LocationDto toDto(LocationData location);
}


