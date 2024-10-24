package com.ortega.account.account;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
public class AccountDTO {

    private UUID accountId;
    private UUID customerId;
    private String accountNumber;
    private BigDecimal balance;
    private Boolean status;

}
