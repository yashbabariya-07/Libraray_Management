package com.lbm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lbm.Enum.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String id;

    @NotNull
    private String name;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull
    private String email;

    @NotNull
    private String address;

    @NotNull
    private Role role;

    @NotNull
    private LocationDto location;

    private String timeZone;

}
