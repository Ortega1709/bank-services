package com.ortega.customer.aggregate;

import com.ortega.customer.command.CreateCustomerCommand;
import com.ortega.customer.customer.Gender;
import com.ortega.customer.event.CustomerCreatedEvent;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.UUID;

@Aggregate
@NoArgsConstructor
public class CustomerAggregate {

    @AggregateIdentifier
    private UUID customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private Gender gender;
    private Date dateOfBirth;

    @CommandHandler
    CustomerAggregate(CreateCustomerCommand createCustomerCommand) {
        CustomerCreatedEvent customerCreatedEvent = new CustomerCreatedEvent();
        BeanUtils.copyProperties(createCustomerCommand, customerCreatedEvent);

        AggregateLifecycle.apply(customerCreatedEvent);
    }

    @EventSourcingHandler
    public void on(CustomerCreatedEvent event) {
        this.customerId = event.getCustomerId();
        this.firstName = event.getFirstName();
        this.lastName = event.getLastName();
        this.email = event.getEmail();
        this.phone = event.getPhone();
        this.address = event.getAddress();
        this.gender = event.getGender();
        this.dateOfBirth = event.getDateOfBirth();
    }
}
