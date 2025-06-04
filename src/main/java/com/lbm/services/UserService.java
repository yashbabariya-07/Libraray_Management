package com.lbm.services;

import com.lbm.dto.response.ApiResponse;
import com.lbm.dto.response.JwtResponse;
import com.lbm.dto.UserDto;
import com.lbm.entity.User;
import com.lbm.exception.EntityNotFoundException;
import com.lbm.mapper.UserMapper;
import com.lbm.repository.UserRepository;
import com.lbm.utils.SecurityUtil;
import com.lbm.validation.BookValidation;
import com.lbm.validation.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final Validation validation;

    public UserDto registerUser(UserDto userDTO) {
        User user = UserMapper.INSTANCE.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return UserMapper.INSTANCE.toDto(userRepository.save(user));
    }

    public JwtResponse login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email);
        validation.validateUserEmail(user);
        validation.validatePassword(user, rawPassword, passwordEncoder);
        return new JwtResponse(jwtService.generateToken(user));
    }

    public UserDto getCurrentUser() {
        return UserMapper.INSTANCE.toDto(userRepository.findByName(
                SecurityUtil.getAuthenticatedUser().getName()).orElseThrow(
                        ()-> new EntityNotFoundException("User", "")));
    }

    public UserDto updateUser(UserDto updatedDto) {
        User currentUser = SecurityUtil.getAuthenticatedUser();
        UserMapper.INSTANCE.updateUser(updatedDto, currentUser);
        if(updatedDto.getPassword() != null){
            currentUser.setPassword(passwordEncoder.encode(updatedDto.getPassword()));
        }
        return UserMapper.INSTANCE.toDto(userRepository.save(currentUser));
    }

    public ApiResponse deleteUser(String userId) {
        User user = userRepository.findUserById(userId,"User");
        BookValidation.validateReturnBook(user, userId);
        userRepository.delete(user);
        return ApiResponse.builder().status(true).message(
                "User with id '" + userId + "' deleted for successfully.").build();
    }
}
