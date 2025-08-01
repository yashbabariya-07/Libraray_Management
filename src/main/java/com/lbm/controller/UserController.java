package com.lbm.controller;

import com.lbm.dto.response.ApiResponse;
import com.lbm.dto.request.JwtRequest;
import com.lbm.dto.response.JwtResponse;
import com.lbm.dto.UserDto;
import com.lbm.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public UserDto registerUser(@Valid @RequestBody UserDto userDTO) {
        return userService.registerUser(userDTO);
    }

    @PostMapping("/login")
    public JwtResponse login(@RequestBody JwtRequest loginRequest) {
        return userService.login(loginRequest.getEmail(), loginRequest.getPassword());
    }

    @GetMapping("/get")
    @SecurityRequirement(name = "Bearer Token")
    public UserDto getUser() {
        return userService.getCurrentUser();
    }

    @PatchMapping("/update")
    @SecurityRequirement(name = "Bearer Token")
    public UserDto updateUser(@RequestBody UserDto userDto){
        return userService.updateUser(userDto);
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @DeleteMapping("/delete/{uid}")
    @SecurityRequirement(name = "Bearer Token")
    public ApiResponse deleteUser(
            @PathVariable String uid) {
        return userService.deleteUser(uid);
    }
}
