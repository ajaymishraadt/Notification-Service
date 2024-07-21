package com.notification_service.notification_service.service;

import com.notification_service.notification_service.dto.SendNotificationDto;

public interface NotificationService {

    SendNotificationDto sendNotification(String templateId, SendNotificationDto sendNotificationDto);
}
