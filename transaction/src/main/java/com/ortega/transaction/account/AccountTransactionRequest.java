package com.ortega.transaction.account;

import jakarta.validation.constraints.Negative;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountTransactionRequest(
        @NotNull(message = "Account id is required")
        UUID accountId,

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount mustn't be negative")
        BigDecimal amount
) {
}
