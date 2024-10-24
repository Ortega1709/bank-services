package com.ortega.customer.customer;

import com.ortega.customer.event.customer.CustomerCreatedEvent;
import com.ortega.customer.event.customer.CustomerDeletedEvent;
import com.ortega.customer.event.customer.CustomerEvent;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import static org.springframework.kafka.support.KafkaHeaders.TOPIC;


@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerKafkaProducer {

    private final KafkaTemplate<String, CustomerEvent> kafkaTemplate;

    public void produceCustomerCreatedEvent(CustomerCreatedEvent event) {
        log.info("Producing customer created event: {}", event);

        Message<CustomerCreatedEvent> customerCreatedMessage = MessageBuilder
                .withPayload(event)
                .setHeader(TOPIC, "customer-created-topic")
                .build();

        kafkaTemplate.send(customerCreatedMessage);
    }

    public void produceCustomerDeletedEvent(CustomerDeletedEvent event) {
        log.info("Producing customer deleted event: {}", event);

        Message<CustomerDeletedEvent> customerCreatedMessage = MessageBuilder
                .withPayload(event)
                .setHeader(TOPIC, "customer-deleted-topic")
                .build();

        kafkaTemplate.send(customerCreatedMessage);
    }
}
