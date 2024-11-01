package com.ortega.transaction.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FeesNotFoundException extends RuntimeException {
    private final String message;
}
