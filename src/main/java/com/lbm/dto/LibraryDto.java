package com.lbm.dto;

import com.lbm.Enum.Days;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.LocalTime;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LibraryDto {

    private String id;

    @NotNull
    private String name;

    @NotNull
    private LocationDto location;

    @NotNull
    private LocalTime openTime;

    @NotNull
    private LocalTime closeTime;

    @NotNull
    private List<Days> openingDay;

    private String librarian;
    private String uniqueId;
    private List<BookDto> books;
}
