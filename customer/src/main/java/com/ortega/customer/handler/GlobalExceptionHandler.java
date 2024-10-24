package com.ortega.customer.handler;

import com.ortega.customer.exception.CustomerAlreadyExistsException;
import com.ortega.customer.exception.CustomerNotFoundException;
import com.ortega.customer.response.ErrorResponse;
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

    @ExceptionHandler(CustomerAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleCustomerAlreadyExistsException(CustomerAlreadyExistsException e) {
        return ErrorResponse.builder()
                .status("error")
                .code(HttpStatus.NOT_FOUND.value())
                .message("Customer already exists")
                .error(e.getMessage())
                .build();
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleCustomerNotFoundException(CustomerNotFoundException e) {
        return ErrorResponse.builder()
                .status("error")
                .code(HttpStatus.NOT_FOUND.value())
                .message("Customer not found")
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
}
