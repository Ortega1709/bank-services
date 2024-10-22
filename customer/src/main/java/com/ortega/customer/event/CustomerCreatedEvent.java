package com.ortega.customer.event;

import com.ortega.customer.customer.Gender;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class CustomerCreatedEvent {
    private UUID customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private Gender gender;
    private Date dateOfBirth;
}
