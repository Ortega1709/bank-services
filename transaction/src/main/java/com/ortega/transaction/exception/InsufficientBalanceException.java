package com.ortega.transaction.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class InsufficientBalanceException extends RuntimeException {
    private final String message;
}
