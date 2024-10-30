package com.ortega.notification.notification;

import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    /**
     * Map Notification to Notification data transfer object.
     *
     * @param notification Object that contains Notification data.
     * @return Object Notification DTO.
     */
    public NotificationDTO toDTO(Notification notification) {
        return NotificationDTO.builder()
                .notificationId(notification.getNotificationId())
                .notificationDate(notification.getNotificationDate())
                .notificationType(notification.getNotificationType())
                .content(notification.getContent())
                .build();
    }

}
