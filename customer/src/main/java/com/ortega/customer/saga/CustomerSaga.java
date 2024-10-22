package com.ortega.customer.saga;

import com.ortega.customer.event.CustomerCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Saga
@Slf4j
@RequiredArgsConstructor
public class CustomerSaga {

    private final CommandGateway commandGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "customerId")
    private void handle(CustomerCreatedEvent event) {
        log.info("CustomerCreatedEvent in saga for customer id: {}", event.getCustomerId());
    }


    private String generateAccountNumber() {
        String accountPrefix = "ACC";

        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        int numberPart = new Random().nextInt(10000);

        return accountPrefix + datePart + String.format("%04d", numberPart);
    }

}
