package com.ortega.customer.account;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {

    private UUID accountId;
    private UUID customerId;
    private String accountNumber;
    private Integer pin;
    private BigDecimal balance;
    private Boolean status;

}
