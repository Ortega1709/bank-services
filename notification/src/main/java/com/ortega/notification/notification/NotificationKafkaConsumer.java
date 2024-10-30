package com.ortega.notification.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ortega.notification.email.EmailService;
import com.ortega.notification.event.account.AccountStatusUpdatedEvent;
import com.ortega.notification.event.customer.CustomerCreatedEvent;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationKafkaConsumer {

    private final EmailService emailService;
    private final ObjectMapper objectMapper;
    private final NotificationRepository notificationRepository;

    /**
     * Consume Account status updated event from Account service.
     *
     * @param event Event from Account Service.
     * @throws MessagingException If email notification is not sent.
     * @throws JsonProcessingException If we get error of reading event with object mapper.
     */
    @KafkaListener(topics = "account-status-updated-topic", groupId = "notification-group")
    public void consumeAccountStatusUpdatedEvent(String event) throws MessagingException, JsonProcessingException {
        log.info("Consuming account status updated event :: {}", event);

        AccountStatusUpdatedEvent accountStatusUpdatedEvent = objectMapper
                .readValue(event, AccountStatusUpdatedEvent.class);

        notificationRepository.save(
                Notification.builder()
                        .notificationId(UUID.randomUUID())
                        .notificationType(
                                accountStatusUpdatedEvent.getStatus() ?
                                        NotificationType.ACTIVATION_ACCOUNT_NOTIFICATION :
                                        NotificationType.DEACTIVATION_ACCOUNT_NOTIFICATION
                        )
                        .notificationDate(LocalDateTime.now())
                        .content(accountStatusUpdatedEvent)
                        .build()
        );

        emailService.sendAccountStatusUpdatedNotification(accountStatusUpdatedEvent);
    }

    /**
     * Consume Customer created event from Customer service.
     *
     * @param event Event from Customer Service.
     * @throws MessagingException If email notification is not sent.
     * @throws JsonProcessingException If we get error reading event with object mapper.
     */
    @KafkaListener(topics = "customer-created-topic", groupId = "notification-group")
    public void consumeCustomerCreatedEvent(String event) throws MessagingException, JsonProcessingException {
        log.info("Consuming customer created event :: {}", event);

        CustomerCreatedEvent customerCreatedEvent = objectMapper
                .readValue(event, CustomerCreatedEvent.class);

        notificationRepository.save(
                Notification.builder()
                        .notificationId(UUID.randomUUID())
                        .notificationType(NotificationType.CREATION_ACCOUNT_NOTIFICATION)
                        .notificationDate(LocalDateTime.now())
                        .content(customerCreatedEvent)
                        .build()
        );

        emailService.sendCustomerCreatedNotification(customerCreatedEvent);
    }
}
