package com.notification_service.notification_service.service;

import com.notification_service.notification_service.dto.RequestDto;

public interface NotificationService {

    RequestDto sendNotification(RequestDto notificationRequest);
}
