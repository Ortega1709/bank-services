package com.ortega.account.account;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ortega.account.event.customer.CustomerCreatedEvent;
import com.ortega.account.event.customer.CustomerDeletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountKafkaListener {


    @KafkaListener(topics = "customer-created-topic", groupId = "account-group")
    public void listenCustomerCreatedEvent(String event) throws JsonProcessingException {
        log.info("Listening customer created event: {}", event);

        CustomerCreatedEvent customerCreatedEvent = new ObjectMapper()
                .readValue(event, CustomerCreatedEvent.class);
    }

    @KafkaListener(topics = "customer-deleted-topic", groupId = "account-group")
    public void listenCustomerDeletedEvent(String event) throws JsonProcessingException {
        log.info("Listening customer deleted event: {}", event);

        CustomerDeletedEvent customerDeletedEvent = new ObjectMapper()
                .readValue(event, CustomerDeletedEvent.class);
    }
}
