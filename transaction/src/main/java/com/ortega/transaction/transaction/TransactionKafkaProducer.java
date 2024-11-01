package com.ortega.transaction.transaction;


import com.ortega.transaction.event.transaction.TransactionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionKafkaProducer {

    private final KafkaTemplate<String, TransactionEvent> kafkaTemplate;



//    /**
//     * Produce account status updated event.
//     *
//     * @param event Object contains information about an account status updated.
//     */
//    public void produceAccountStatusUpdatedEvent(AccountStatusUpdatedEvent event) {
//        log.info("Producing account status updated event:: {}", event);
//
//        Message<AccountStatusUpdatedEvent> message = MessageBuilder
//                .withPayload(event)
//                .setHeader(TOPIC, "account-status-updated-topic")
//                .build();
//
//        kafkaTemplate.send(message);
//    }

}
