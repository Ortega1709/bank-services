package com.ortega.customer.event.customer;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
public class CustomerCreatedEvent implements CustomerEvent {
    private UUID customerId;
    private String firstName;
    private String lastName;
}
