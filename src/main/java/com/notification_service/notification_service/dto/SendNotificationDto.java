package com.notification_service.notification_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SendNotificationDto {

    private String templateId;
    private String sender;
    private String subject;
    private String htmlBody;
    private List<String> emailIds;


}
