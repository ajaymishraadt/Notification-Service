package com.notification_service.notification_service.controller.api;

import com.notification_service.notification_service.dto.NotificationRequestDto;
import com.notification_service.notification_service.dto.RequestDto;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/notification")
public interface NotificationApi {

    @PostMapping("/send")
    public RequestDto sendNotification(
            @RequestBody(required = false) RequestDto notificationRequest);
}
