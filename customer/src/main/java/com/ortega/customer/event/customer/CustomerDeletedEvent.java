package com.ortega.customer.event.customer;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
public class CustomerDeletedEvent implements CustomerEvent {
    private UUID customerId;
}
