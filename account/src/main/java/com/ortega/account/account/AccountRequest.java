package com.ortega.account.account;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountRequest(
        UUID accountId,
        @NotNull(message = "Customer id is required")
        UUID customerId,
        String accountNumber,
        BigDecimal balance,
        Boolean status
) {
}
