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
    private void listenCustomerCreatedEvent(CustomerCreatedEvent event) {
        log.info("Listening customer created event: {}", event);
    }

    @KafkaListener(topics = "customer-deleted-topic", groupId = "account-group")
    private void listenCustomerDeletedEvent(CustomerDeletedEvent event) {
        log.info("Listening customer deleted event: {}", event);
    }
}
