package com.notification_service.notification_service.service;

import com.notification_service.notification_service.dto.SendNotificationDto;
import com.notification_service.notification_service.dto.TemplateDetailsDto;

public interface NotificationService {

    SendNotificationDto sendNotification(String templateName, SendNotificationDto sendNotificationDto);
}
