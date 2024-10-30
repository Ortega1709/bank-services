package com.ortega.notification.notification;

import com.ortega.notification.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponse findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(page, size);
        Page<NotificationDTO> notificationDTOS = notificationService.findAll(pageable);

        return SuccessResponse.builder()
                .status("success")
                .message("Notifications fetched successfully")
                .code(HttpStatus.OK.value())
                .data(notificationDTOS)
                .build();
    }
}
