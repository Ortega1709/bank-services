package com.ortega.user.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class UserNotFoundException extends RuntimeException {
    private final String message;
}
