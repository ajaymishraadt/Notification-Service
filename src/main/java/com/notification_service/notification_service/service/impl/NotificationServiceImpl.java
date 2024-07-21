package com.notification_service.notification_service.service.impl;

import com.notification_service.notification_service.dto.SendNotificationDto;
import com.notification_service.notification_service.entity.Template;
import com.notification_service.notification_service.entity.User;
import com.notification_service.notification_service.repository.TemplateRepository;
import com.notification_service.notification_service.repository.UserRepository;
import com.notification_service.notification_service.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${sender}")
    private String DEFAULT_SENDER;

    @Override
    public SendNotificationDto sendNotification(String templateId, SendNotificationDto sendNotificationDto) {
        try {
            if (!ObjectUtils.isEmpty(templateId)) {
                return processTemplateId(templateId);
            }

            if (!ObjectUtils.isEmpty(sendNotificationDto.getTemplateId())) {
                return processSendNotificationDto(sendNotificationDto);
            }

            logger.warn("Both templateId and sendNotificationDto are empty.");
            return null;
        } catch (Exception e) {
            logger.error("Error occurred while sending notification: ", e);
            throw new RuntimeException("Failed to send notification", e);
        }
    }

    private SendNotificationDto processTemplateId(String templateId) {
        logger.info("Fetching template details for templateId: {}", templateId);
        Optional<Template> optionalTemplate = templateRepository.findByTemplateId(templateId);

        if (optionalTemplate.isEmpty()) {
            logger.warn("Template not found for templateId: {}", templateId);
            throw new RuntimeException("Template not found for templateId: " + templateId);
        }

        Template template = optionalTemplate.get();
        logger.info("Successfully fetched template details for templateId: {}", templateId);

        return SendNotificationDto.builder()
                .templateId(templateId)
                .sender(DEFAULT_SENDER)
                .htmlBody(template.getBody())
                .subject(template.getSubject())
                .emailIds(getEmailIds(templateId))
                .build();
    }

    private SendNotificationDto processSendNotificationDto(SendNotificationDto sendNotificationDto) {
        logger.info("Using provided SendNotificationDto template details.");
        SendNotificationDto.SendNotificationDtoBuilder notificationDtoBuilder = sendNotificationDto.toBuilder();

        notificationDtoBuilder.sender(
                ObjectUtils.isEmpty(sendNotificationDto.getSender()) ? DEFAULT_SENDER : sendNotificationDto.getSender()
        );

        Optional<Template> existingDetailsOptional = templateRepository.findByTemplateId(sendNotificationDto.getTemplateId());

        if (existingDetailsOptional.isEmpty()) {
            logger.warn("Template not found for templateId: {}", sendNotificationDto.getTemplateId());
            throw new RuntimeException("Template not found for templateId: " + sendNotificationDto.getTemplateId());
        }

        Template existingDetails = existingDetailsOptional.get();

        notificationDtoBuilder.htmlBody(
                ObjectUtils.isEmpty(sendNotificationDto.getHtmlBody()) ? existingDetails.getBody() : sendNotificationDto.getHtmlBody()
        );

        notificationDtoBuilder.emailIds(
                ObjectUtils.isEmpty(sendNotificationDto.getEmailIds()) ? getEmailIds(sendNotificationDto.getTemplateId()) : sendNotificationDto.getEmailIds()
        );

        notificationDtoBuilder.subject(
                ObjectUtils.isEmpty(sendNotificationDto.getSubject()) ? existingDetails.getSubject() : sendNotificationDto.getSubject()
        );

        logger.info("Successfully built SendNotificationDto from provided template details.");
        return notificationDtoBuilder.build();
    }

    private List<String> getEmailIds(String templateId) {
        logger.info("Fetching email IDs for templateId: {}", templateId);
        List<User> users = userRepository.findAllByTemplatesTemplateId(templateId);
        List<String> emailIds = users.stream()
                .map(User::getEmail)
                .collect(Collectors.toList());
        logger.info("Successfully fetched {} email IDs for templateId: {}", emailIds.size(), templateId);
        return emailIds;
    }
}

