package com.ortega.customer.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.UUID;

public record CustomerRequest(
        UUID customerId,
        @NotNull(message = "Customer first name is required")
        String firstName,
        @NotNull(message = "Customer last name is required")
        String lastName,
        @NotNull(message = "Customer email is required")
        @Email(message = "Customer email is not valid")
        String email,
        @NotNull(message = "Customer phone is required")
        String phone,
        @NotNull(message = "Customer address is required")
        String address,
        @NotNull(message = "Customer gender is required")
        Gender gender,
        @NotNull(message = "Customer birth date is required")
        Date dateOfBirth
) {
}
