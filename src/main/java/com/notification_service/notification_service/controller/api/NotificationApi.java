package com.notification_service.notification_service.controller.api;

import com.notification_service.notification_service.dto.SendNotificationDto;
import com.notification_service.notification_service.dto.TemplateDetailsDto;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/notification")
public interface NotificationApi {

    @PostMapping("/send/{templateName}")
    public SendNotificationDto sendNotification(
            @PathVariable(value = "templateName", required = false) String templateName,
            @RequestBody(required = false) SendNotificationDto sendNotificationDto );
}
