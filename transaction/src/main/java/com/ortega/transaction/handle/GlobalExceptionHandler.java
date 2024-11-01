package com.ortega.transaction.handle;

import com.ortega.transaction.exception.BusinessException;
import com.ortega.transaction.exception.FeesNotFoundException;
import com.ortega.transaction.exception.InsufficientBalanceException;
import com.ortega.transaction.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle Exception.
     *
     * @param e contains exception message.
     * @return Object Error Response.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception e) {
        return ErrorResponse.builder()
                .status("error")
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Error occurred while processing request")
                .error(e.getMessage())
                .build();
    }

    /**
     * Handle FeesNotFoundException.
     *
     * @param e contains exception message.
     * @return Object Error Response.
     */
    @ExceptionHandler(FeesNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFeesNotFoundException(FeesNotFoundException e) {
        return ErrorResponse.builder()
                .status("error")
                .code(HttpStatus.NOT_FOUND.value())
                .message("Fee not found")
                .error(e.getMessage())
                .build();
    }

    /**
     * Handle InsufficientBalanceException.
     *
     * @param e contains exception message.
     * @return Object ErrorResponse.
     */
    @ExceptionHandler(InsufficientBalanceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInsufficientBalanceException(InsufficientBalanceException e) {
        return ErrorResponse.builder()
                .status("error")
                .code(HttpStatus.BAD_REQUEST.value())
                .message("Insufficient balance")
                .error(e.getMessage())
                .build();
    }

    /**
     * Handle BusinessException.
     *
     * @param e contains exception message.
     * @return Object Error Response.
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleBusinessException(BusinessException e) {
        return ErrorResponse.builder()
                .status("error")
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Error occurred while processing request")
                .error(e.getMessage())
                .build();
    }

}
