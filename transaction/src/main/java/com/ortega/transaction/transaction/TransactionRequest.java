package com.ortega.transaction.transaction;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransactionRequest(
        @NotNull(message = "Account ID is required")
        String accountNumber,
        String toAccountNumber,

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount mustn't be negative")
        BigDecimal amount
) {
}
