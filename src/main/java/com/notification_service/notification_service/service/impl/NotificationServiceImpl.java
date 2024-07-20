package com.notification_service.notification_service.service.impl;

import com.notification_service.notification_service.dto.SendNotificationDto;
import com.notification_service.notification_service.dto.TemplateDetailsDto;
import com.notification_service.notification_service.repository.TemplateRepo;
import com.notification_service.notification_service.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    TemplateRepo templateRepo;

    @Value("${sender}")
    private String DEFAULT_SENDER;

    @Override
    public SendNotificationDto sendNotification(String templateName, SendNotificationDto sendNotificationDto) {
            try {
                if (!ObjectUtils.isEmpty(templateName)) {
                    TemplateDetailsDto details = templateRepo.findTemplateDetailsByTemplateName(templateName);
                    return buildSendNotificationDto(details, templateName, DEFAULT_SENDER);
                }

                if (!ObjectUtils.isEmpty(sendNotificationDto.getTemplateName())) {
                    SendNotificationDto.SendNotificationDtoBuilder notificationDtoBuilder = sendNotificationDto.toBuilder();

                    notificationDtoBuilder.sender(
                            ObjectUtils.isEmpty(sendNotificationDto.getSender()) ? DEFAULT_SENDER : sendNotificationDto.getSender()
                    );

                    TemplateDetailsDto existingDetails = templateRepo.findTemplateDetailsByTemplateName(sendNotificationDto.getTemplateName());
                    TemplateDetailsDto.TemplateDetailsDtoBuilder detailsDtoBuilder = existingDetails.toBuilder();

                    detailsDtoBuilder.htmlBody(
                            ObjectUtils.isEmpty(sendNotificationDto.getTemplateDetails().getHtmlBody())
                                    ? existingDetails.getHtmlBody()
                                    : sendNotificationDto.getTemplateDetails().getHtmlBody()
                    );

                    detailsDtoBuilder.emailIds(
                            ObjectUtils.isEmpty(sendNotificationDto.getTemplateDetails().getEmailIds())
                                    ? existingDetails.getEmailIds()
                                    : sendNotificationDto.getTemplateDetails().getEmailIds()
                    );

                    detailsDtoBuilder.subject(
                            ObjectUtils.isEmpty(sendNotificationDto.getTemplateDetails().getSubject())
                                    ? existingDetails.getSubject()
                                    : sendNotificationDto.getTemplateDetails().getSubject()
                    );

                    return notificationDtoBuilder.templateDetails(detailsDtoBuilder.build()).build();
                }

                logger.warn("Both templateName and sendNotificationDto are empty.");
                return null;
            } catch (Exception e) {
                logger.error("Error occurred while sending notification: ", e);
                throw new RuntimeException("Failed to send notification", e);
            }
        }

        private SendNotificationDto buildSendNotificationDto(TemplateDetailsDto details, String templateName, String sender) {
            return SendNotificationDto.builder()
                    .templateDetails(details)
                    .sender(sender)
                    .templateName(templateName)
                    .build();
        }
}
