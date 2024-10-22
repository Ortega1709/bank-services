package com.ortega.customer.customer;

import com.ortega.customer.command.CreateCustomerCommand;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CommandGateway commandGateway;

    public CustomerDTO createCustomer(CustomerRequest request) {

        UUID customerId = UUID.randomUUID();
        CreateCustomerCommand command = CreateCustomerCommand.builder()
                .customerId(customerId)
                .firstName(request.firstName())
                .lastName(request.lastName())
                .address(request.address())
                .email(request.email())
                .phone(request.phone())
                .gender(request.gender())
                .dateOfBirth(request.dateOfBirth())
                .build();

        commandGateway.sendAndWait(command);
        return CustomerDTO.builder()
                .customerId(customerId)
                .firstName(request.firstName())
                .lastName(request.lastName())
                .address(request.address())
                .email(request.email())
                .phone(request.phone())
                .gender(request.gender())
                .dateOfBirth(request.dateOfBirth())
                .build();
    }
}
