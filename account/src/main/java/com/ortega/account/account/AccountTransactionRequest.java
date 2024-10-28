package com.ortega.account.account;

import jakarta.validation.constraints.Negative;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountTransactionRequest(
        @NotNull(message = "Account id is required")
        UUID accountId,

        @NotNull(message = "Amount is required")
        @Negative(message = "Amount mustn't be negative")
        @NotBlank(message = "Amount mustn't be blank")
        BigDecimal amount
) {
}
