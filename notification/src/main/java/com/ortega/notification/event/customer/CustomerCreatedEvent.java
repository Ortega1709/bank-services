package com.ortega.notification.event.customer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
