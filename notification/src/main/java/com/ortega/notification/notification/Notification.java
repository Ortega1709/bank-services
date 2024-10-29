package com.ortega.notification.notification;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@Document
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    private UUID notificationId;
    private NotificationType notificationType;
    private LocalDateTime notificationDate;
    private Object content;

}
