package com.notification_service.notification_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TemplateDto {
    private String id;
    private String data;
    private String type;
    private ConfigDto config;
}
