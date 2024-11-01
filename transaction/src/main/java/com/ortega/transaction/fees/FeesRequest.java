package com.ortega.transaction.fees;

import com.ortega.transaction.transaction.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record FeesRequest(
        UUID feesId,
        @NotNull(message = "Transaction type is required")
        TransactionType transactionType,

        @NotNull(message = "Percent is required")
        @Positive(message = "Percent mustn't be negative")
        Double percent,

        @NotNull(message = "Min amount is required")
        @Positive(message = "Min amount mustn't be negative")
        BigDecimal minAmount,

        @NotNull(message = "Max amount is required")
        @Positive(message = "Max amount mustn't be negative")
        BigDecimal maxAmount
) {
}
