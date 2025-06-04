package com.lbm.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequest {

    @NotNull
    private String email;

    @NotNull
    private String password;

}
