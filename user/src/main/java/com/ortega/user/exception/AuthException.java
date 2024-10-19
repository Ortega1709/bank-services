package com.ortega.user.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class AuthException extends RuntimeException {
    private final String message;
}
