package com.ortega.transaction.fees;

import com.ortega.transaction.transaction.TransactionType;
import jakarta.annotation.PostConstruct;
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
@Entity(name = "fees")
public class Fees {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID feesId;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    private Double percent;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
