package com.lbm.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    private String id;

    @NotNull
    private String name;

    @NotNull
    private String author;

    @NotNull
    private String publisher;

    @NotNull
    private int totalAvailableQuantity;

    @NotNull
    private LocalTime totalAvailableTime;

}
