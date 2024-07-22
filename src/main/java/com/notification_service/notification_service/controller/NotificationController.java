package com.notification_service.notification_service.controller;

import com.notification_service.notification_service.controller.api.NotificationApi;
import com.notification_service.notification_service.dto.RequestDto;
import com.notification_service.notification_service.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController implements NotificationApi {

    @Autowired
    private NotificationService notificationService;

    @Override
    public RequestDto sendNotification(RequestDto notificationRequest) {
        return notificationService.sendNotification(notificationRequest);
    }
}
