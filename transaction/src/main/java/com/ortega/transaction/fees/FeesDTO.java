package com.ortega.transaction.fees;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ortega.transaction.transaction.TransactionType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
public class FeesDTO {

    private UUID feesId;
    private TransactionType transactionType;
    private Double percent;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

}
