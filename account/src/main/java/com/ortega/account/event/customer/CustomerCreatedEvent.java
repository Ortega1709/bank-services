package com.ortega.account.event.customer;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CustomerCreatedEvent implements CustomerEvent {
    private UUID customerId;
    private String firstName;
    private String lastName;
}
