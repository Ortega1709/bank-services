package com.ortega.customer.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class CustomerAlreadyExistsException extends RuntimeException {
    private final String message;
}
