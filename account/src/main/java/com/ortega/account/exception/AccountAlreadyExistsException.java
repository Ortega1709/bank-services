package com.ortega.account.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AccountAlreadyExistsException extends RuntimeException {
    private final String message;
}
