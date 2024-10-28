package com.ortega.account.account;

import com.ortega.account.event.account.AccountEvent;
import com.ortega.account.event.account.AccountStatusUpdatedEvent;
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

    public void produceAccountStatusUpdatedEvent(AccountStatusUpdatedEvent event) {
        log.info("Producing account status updated event: {}", event);

        Message<AccountStatusUpdatedEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(TOPIC, "account-status-updated-topic")
                .build();

        kafkaTemplate.send(message);
    }


}
