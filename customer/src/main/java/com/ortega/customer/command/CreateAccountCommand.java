package com.ortega.customer.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class CreateAccountCommand {

    @TargetAggregateIdentifier
    private UUID accountId;
    private UUID customerId;
    private String accountNumber;
    private BigDecimal balance;
    private boolean status;

}
