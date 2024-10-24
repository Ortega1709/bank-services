package com.ortega.customer.event.customer;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerCreatedEvent implements CustomerEvent {
    private UUID customerId;
    private String firstName;
    private String lastName;
}
