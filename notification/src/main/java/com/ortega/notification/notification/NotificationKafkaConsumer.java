package com.ortega.notification.notification;

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

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    @KafkaListener(topics = "account-status-updated-topic")
    public void consumeAccountStatusUpdatedEvent(AccountStatusUpdatedEvent event) throws MessagingException {
        log.info("Consuming account status updated topic :: {}", event);
        notificationRepository.save(
                Notification.builder()
                        .notificationId(UUID.randomUUID())
                        .notificationType(
                                event.getStatus() ?
                                        NotificationType.ACTIVATION_ACCOUNT_NOTIFICATION :
                                        NotificationType.DEACTIVATION_ACCOUNT_NOTIFICATION
                        )
                        .notificationDate(LocalDateTime.now())
                        .content(event)
                        .build()
        );

        emailService.sendAccountStatusUpdatedNotification(event);
    }

    @KafkaListener(topics = "customer-created-topic")
    public void consumeCustomerCreatedEvent(CustomerCreatedEvent event) throws MessagingException {
        log.info("Consuming customer created topic :: {}", event);
        notificationRepository.save(
                Notification.builder()
                        .notificationId(UUID.randomUUID())
                        .notificationType(NotificationType.CREATION_ACCOUNT_NOTIFICATION)
                        .notificationDate(LocalDateTime.now())
                        .content(event)
                        .build()
        );

        emailService.sendCustomerCreatedNotification(event);
    }
}
