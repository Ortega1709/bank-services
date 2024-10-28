package com.ortega.customer.event.customer;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerCreatedEvent implements CustomerEvent {
    private String accountNumber;
    private String email;
    private String firstName;
    private String lastName;
    private Integer pin;
}
