package com.notification_service.notification_service.controller;

import com.notification_service.notification_service.controller.api.NotificationApi;
import com.notification_service.notification_service.dto.SendNotificationDto;
import com.notification_service.notification_service.dto.TemplateDetailsDto;
import com.notification_service.notification_service.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController implements NotificationApi {

    @Autowired
    private NotificationService notificationService;

    @Override
    public SendNotificationDto sendNotification(String templateName, SendNotificationDto sendNotificationDto) {
        return notificationService.sendNotification(templateName,sendNotificationDto);
    }
}
