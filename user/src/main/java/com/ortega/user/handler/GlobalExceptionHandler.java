package com.ortega.user.handler;

import com.ortega.user.exception.AuthException;
import com.ortega.user.exception.RoleNotFoundException;
import com.ortega.user.exception.UserAlreadyExistsException;
import com.ortega.user.exception.UserNotFoundException;
import com.ortega.user.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(UserNotFoundException e) {
        return ErrorResponse.builder()
                .status("error")
                .code(HttpStatus.NOT_FOUND.value())
                .message("User not found")
                .error(e.getMessage())
                .build();
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return ErrorResponse.builder()
                .status("error")
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("User already exists")
                .error(e.getMessage())
                .build();
    }

    @ExceptionHandler(RoleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleRoleNotFoundException(RoleNotFoundException e) {
        return ErrorResponse.builder()
                .status("error")
                .code(HttpStatus.NOT_FOUND.value())
                .message("Role not found")
                .error(e.getMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        var errors = new HashMap<String, String>();
        e.getBindingResult().getAllErrors()
                .forEach(error -> {
                    var fieldName = ((FieldError) error).getField();
                    var errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });

        return ErrorResponse.builder()
                .status("error")
                .code(HttpStatus.BAD_REQUEST.value())
                .message("Invalid request")
                .error("Fields are invalid")
                .errors(errors)
                .build();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return ErrorResponse.builder()
                .status("error")
                .code(HttpStatus.BAD_REQUEST.value())
                .message("Invalid argument")
                .error(e.getMessage())
                .build();
    }

    @ExceptionHandler(AuthException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleAuthException(AuthException e) {
        return ErrorResponse.builder()
                .status("error")
                .code(HttpStatus.BAD_REQUEST.value())
                .message("Authentication error")
                .error(e.getMessage())
                .build();
    }
}
