package com.ortega.customer.customer;

import com.ortega.customer.event.customer.CustomerCreatedEvent;
import com.ortega.customer.event.customer.CustomerDeletedEvent;
import com.ortega.customer.event.customer.CustomerEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;



@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerKafkaProducer {

    private final KafkaTemplate<String, CustomerEvent> kafkaTemplate;

    public void produceCustomerCreatedEvent(CustomerCreatedEvent event) {
        log.info("Producing customer created event: {}", event);
        kafkaTemplate.send("customer-created-topic", event);
    }

    public void produceCustomerDeletedEvent(CustomerDeletedEvent event) {
        log.info("Producing customer deleted event: {}", event);
        kafkaTemplate.send("customer-deleted-topic", event);
    }
}
