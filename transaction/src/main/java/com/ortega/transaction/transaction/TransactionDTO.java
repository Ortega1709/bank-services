package com.ortega.transaction.transaction;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class TransactionDTO {

    private UUID transactionId;
    private String reference;
    private BigDecimal amount;
    private BigDecimal feesAmount;
    private TransactionType transactionType;
    private TransactionStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime transactionDate;



}
