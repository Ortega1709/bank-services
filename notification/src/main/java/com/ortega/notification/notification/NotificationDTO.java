package com.ortega.notification.notification;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class NotificationDTO {

    private UUID notificationId;
    private NotificationType notificationType;
    private LocalDateTime notificationDate;
    private Object content;

}
