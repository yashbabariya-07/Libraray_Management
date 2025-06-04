package com.lbm.config;

import com.lbm.dto.response.ApiResponse;
import com.lbm.exception.EntityNotFoundException;
import com.lbm.utils.MongoErrorMessageUtil;
import com.mongodb.MongoWriteException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse handleEntity(EntityNotFoundException ex) {
        return ApiResponse.builder().status(false).message(ex.getMessage()).build();
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse handleBadEntity(BadCredentialsException ex) {
        return ApiResponse.builder().status(false).message(ex.getMessage()).build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiResponse handleConflictEntity(IllegalArgumentException ex) {
        return ApiResponse.builder().status(false).message(ex.getMessage()).build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponse handleAccessDeniedException(AccessDeniedException ex) {
        return ApiResponse.builder().status(false).message(
                "You do not have permission to access this resource.").build();
    }

    @ExceptionHandler(MongoWriteException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse handleMongoException(MongoWriteException ex) {
        String rawMessage = ex.getMessage();
        String message = MongoErrorMessageUtil.prepareMongoError(rawMessage);
        return ApiResponse.builder().status(false).message(message).build();
    }

}
