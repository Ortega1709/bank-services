package com.ortega.account.account;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID accountId;
    private UUID customerId;
    private String accountNumber;

    @Column(length = 6)
    private Integer pin;
    private BigDecimal balance;
    private Boolean status;

}
