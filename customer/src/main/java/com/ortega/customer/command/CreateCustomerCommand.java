package com.ortega.customer.command;

import com.ortega.customer.customer.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class CreateCustomerCommand {

    @TargetAggregateIdentifier
    private UUID customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private Gender gender;
    private Date dateOfBirth;

}
