package com.lbm.mapper;

import com.lbm.entity.Book;
import com.lbm.entity.BookStatus;
import com.lbm.entity.User;
import com.lbm.utils.AppTimeZoneUtil;
import com.lbm.utils.GlobalMapping;
import com.lbm.utils.SecurityUtil;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.Date;

@Mapper(imports = {LocalDateTime.class, AppTimeZoneUtil.class, SecurityUtil.class}, config = GlobalMapping.class)
public interface BookStatusMapper {

    BookStatusMapper INSTANCE = Mappers.getMapper(BookStatusMapper.class);

    @Mapping(target = "borrowed", constant = "true")
    @Mapping(target = "borrowedAt", expression = "java(toUTCZone(borrowedAt, user))")
    @Mapping(target = "returnedAt", expression = "java(toUTCZone(returnedAt, user))")
    @Mapping(target = "borrowedBy", source = "user")
    @Mapping(target = "book", source = "book")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "returnDate", ignore = true)
    BookStatus toBookStatus(User user, Book book, LocalDateTime borrowedAt, LocalDateTime returnedAt);

    @Mapping(target = "borrowed", constant = "false")
    @Mapping(target = "returnDate", expression = "java(toUTCDateTime())")
    void updateStatusForReturn(BookStatus source, @MappingTarget BookStatus target);

    default Date toUTCZone(LocalDateTime localDateTime, User user) {
        return AppTimeZoneUtil.toUTCDateTime(localDateTime, user.getTimeZone());
    }

    default Date toUTCDateTime() {
        DateTimeZone userZone = DateTimeZone.forID(SecurityUtil.getAuthenticatedUser().getTimeZone());
        LocalDateTime userNow = LocalDateTime.now(userZone);
        return AppTimeZoneUtil.toUTCDateTime(userNow, userZone.getID());
    }
}

