package com.ortega.account.event.customer;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerDeletedEvent implements CustomerEvent {
    private UUID customerId;
}
