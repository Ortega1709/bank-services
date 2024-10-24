package com.ortega.account.account;

import com.ortega.account.event.account.AccountCreatedEvent;
import com.ortega.account.event.account.AccountCreationFailedEvent;
import com.ortega.account.event.account.AccountEvent;
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
public class AccountKafkaProducer {

    private final KafkaTemplate<String, AccountEvent> kafkaTemplate;

    public void produceAccountCreatedEvent(AccountCreatedEvent event) {
        log.info("Producing account created event: {}", event);

        Message<AccountCreatedEvent> accountCreatedEventMessage = MessageBuilder
                .withPayload(event)
                .setHeader(TOPIC, "account-created-topic")
                .build();

        kafkaTemplate.send(accountCreatedEventMessage);
    }

    public void produceAccountCreationFailedEvent(AccountCreationFailedEvent event) {
        log.info("Producing account creation failed event: {}", event);

        Message<AccountCreationFailedEvent> accountCreationFailedEventMessage = MessageBuilder
                .withPayload(event)
                .setHeader(TOPIC, "account-creation-failed-topic")
                .build();

        kafkaTemplate.send(accountCreationFailedEventMessage);
    }
}
