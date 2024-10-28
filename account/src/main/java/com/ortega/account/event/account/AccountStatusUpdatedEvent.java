package com.ortega.account.event.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountStatusUpdatedEvent implements AccountEvent {
    private String firstName;
    private String lastName;
    private String email;
    private Boolean status;
}
