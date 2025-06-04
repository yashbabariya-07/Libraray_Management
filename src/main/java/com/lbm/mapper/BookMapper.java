package com.lbm.mapper;

import com.lbm.dto.BookDto;
import com.lbm.entity.Book;
import com.lbm.entity.Library;
import com.lbm.utils.AppTimeZoneUtil;
import com.lbm.utils.GlobalMapping;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(config = GlobalMapping.class, uses = {AppTimeZoneUtil.class})
public interface BookMapper {

    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    @Mapping(source = "totalAvailableTime", target = "totalAvailableTime", qualifiedByName = "stringToLocalTime")
    BookDto toDto(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "library", ignore = true)
    @Mapping(source = "totalAvailableTime", target = "totalAvailableTime", qualifiedByName = "localTimeToString")
    Book toEntity(BookDto bookDto);

    List<BookDto> toDtoList(List<Book> books);

    @Mapping(target = "library", source = "library")
    @Mapping(target = "id", source = "dto.id")
    @Mapping(target = "name", source = "dto.name")
    @Mapping(source = "dto.totalAvailableTime", target = "totalAvailableTime", qualifiedByName = "localTimeToString")
    Book toEntity(BookDto dto, Library library);

    @Mapping(target = "author", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "publisher", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "library", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "totalAvailableTime", source = "totalAvailableTime", qualifiedByName = "localTimeToString")
    void updateBookFromDto(BookDto bookDto, @MappingTarget Book book);

}