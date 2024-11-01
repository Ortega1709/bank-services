package com.ortega.transaction.transaction;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID transactionId;
    private String reference;
    private UUID accountId;
    private UUID toAccountId;
    private BigDecimal fees;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;
    private LocalDateTime transactionDate;

}
