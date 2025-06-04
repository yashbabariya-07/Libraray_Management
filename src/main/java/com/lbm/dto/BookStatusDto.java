package com.lbm.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookStatusDto {

    private boolean borrowed;
    private String borrowedBy;
    private LocalDateTime borrowedAt;

    @NotNull
    private LocalDateTime returnedAt;
}
