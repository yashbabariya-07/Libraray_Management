package com.lbm.mapper;

import com.lbm.dto.UserDto;
import com.lbm.entity.User;
import com.lbm.utils.GlobalMapping;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(uses = LocationMapper.class, config = GlobalMapping.class)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    User toEntity(UserDto userDto);

    UserDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUser(UserDto userDto, @MappingTarget User user);

}
