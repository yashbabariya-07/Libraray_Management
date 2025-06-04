package com.lbm.mapper;

import com.lbm.dto.LibraryDto;
import com.lbm.entity.Library;
import com.lbm.entity.User;
import com.lbm.utils.GlobalMapping;
import com.lbm.utils.SecurityUtil;
import com.lbm.utils.AppTimeZoneUtil;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = {BookMapper.class, LocationMapper.class},
        config = GlobalMapping.class,
        imports = {AppTimeZoneUtil.class, SecurityUtil.class})
public interface LibraryMapper {

    LibraryMapper INSTANCE = Mappers.getMapper(LibraryMapper.class);

    @Mapping(target = "user", source = "user")
    @Mapping(target = "books", source = "libraryDto.books")
    @Mapping(target = "name", source = "libraryDto.name")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uniqueId", source = "libraryDto", qualifiedByName = "generateUniqueId")
    @Mapping(target = "location", source = "libraryDto.location")
    @Mapping(target = "openTime", expression = "java(toUtcTime(libraryDto.getOpenTime(), user))")
    @Mapping(target = "closeTime", expression = "java(toUtcTime(libraryDto.getCloseTime(), user))")
    Library toEntity(LibraryDto libraryDto, User user);

    @Mapping(target = "librarian", source = "user.name")
    @Mapping(target = "openTime", source = "openTime", qualifiedByName = "userTime")
    @Mapping(target = "closeTime", source = "closeTime", qualifiedByName = "userTime")
    LibraryDto toDto(Library library);

    List<LibraryDto> toDtoList(List<Library> libraries);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "uniqueId", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "openTime", source = "openTime", qualifiedByName = "updateTime")
    @Mapping(target = "closeTime", source = "closeTime", qualifiedByName = "updateTime")
    void updateLibraryFromDto(LibraryDto libraryDto, @MappingTarget Library library);

    @Named("generateUniqueId")
    default String generateUniqueId(LibraryDto dto) {
        if (dto.getUniqueId() != null && !dto.getUniqueId().trim().isEmpty()) {
            return dto.getUniqueId();
        }

        return dto.getName().replaceAll("\\s+", "") + LocalDate.now().toString("ddMMyy");
    }

    default String toUtcTime(LocalTime localTime, User user) {
        return AppTimeZoneUtil.toUTC(localTime, user.getTimeZone());
    }

    @Named("userTime")
    default LocalTime userTime(String utcTimeStr) {
        return AppTimeZoneUtil.toUserTime(utcTimeStr, SecurityUtil.getAuthenticatedUser().getTimeZone());
    }

    @Named("updateTime")
    default String updateTime(LocalTime localTime) {
        return AppTimeZoneUtil.toUTC(localTime, SecurityUtil.getAuthenticatedUser().getTimeZone());
    }

}
