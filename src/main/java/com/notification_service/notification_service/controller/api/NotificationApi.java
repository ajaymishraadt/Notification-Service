package com.notification_service.notification_service.controller.api;

import com.notification_service.notification_service.dto.SendNotificationDto;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/notification")
public interface NotificationApi {

    @PostMapping("/send/{templateId}")
    public SendNotificationDto sendNotification(
            @PathVariable(value = "templateId", required = false) String templateId,
            @RequestBody(required = false) SendNotificationDto sendNotificationDto );
}
