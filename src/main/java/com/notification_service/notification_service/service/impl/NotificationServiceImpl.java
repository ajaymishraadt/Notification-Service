package com.notification_service.notification_service.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.notification_service.notification_service.dto.*;
import com.notification_service.notification_service.entity.Template;
import com.notification_service.notification_service.entity.User;
import com.notification_service.notification_service.producer.KafkaProducerConfig;
import com.notification_service.notification_service.repository.TemplateRepository;
import com.notification_service.notification_service.repository.UserRepository;
import com.notification_service.notification_service.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
    public RequestDto sendNotification(RequestDto notificationRequest) {
        KafkaProducerConfig kafkaProducerConfig = new KafkaProducerConfig();
        try {
            if (notificationRequest != null && notificationRequest.getRequest() != null
                    && !ObjectUtils.isEmpty(notificationRequest.getRequest().getNotifications())) {

                NotificationRequestDto notificationRequestDto = notificationRequest.getRequest();
                NotificationRequestDto result = processNotificationRequestDto(notificationRequestDto);

                // Convert RequestDto to JSON string (or other string representation)
                String resultString = convertToString(new RequestDto(result));

                // Send the whole RequestDto object as a string with a null or blank key
                kafkaProducerConfig.sendMessage(
                        "send.notification",
                        "",  // Blank key
                        resultString
                );

                return new RequestDto(result);
            }

            logger.warn("RequestDto or NotificationRequestDto is null or empty.");
            return new RequestDto(); // Returning an empty RequestDto
        } catch (Exception e) {
            logger.error("Error occurred while sending send.notification: ", e);
            throw new RuntimeException("Failed to send notification", e);
        } finally {
            kafkaProducerConfig.close();
        }
    }

    private String convertToString(Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert object to string", e);
        }
    }

    private NotificationRequestDto processNotificationRequestDto(NotificationRequestDto notificationRequestDto) {
        List<NotificationDto> updatedNotifications = notificationRequestDto.getNotifications().stream()
                .map(this::processSingleNotification)
                .collect(Collectors.toList());

        return NotificationRequestDto.builder()
                .notifications(updatedNotifications)
                .build();
    }

    private NotificationDto processSingleNotification(NotificationDto notificationDto) {
        TemplateDto templateDto = notificationDto.getAction().getTemplate();
        logger.info("Using provided NotificationRequestDto template details.");

        Optional<Template> existingDetailsOptional = templateRepository.findByTemplateId(templateDto.getId());

        if (existingDetailsOptional.isEmpty()) {
            logger.warn("Template not found for templateId: {}", templateDto.getId());
            throw new RuntimeException("Template not found for templateId: " + templateDto.getId());
        }

        Template existingDetails = existingDetailsOptional.get();

        ConfigDto config = ConfigDto.builder()
                .sender(ObjectUtils.isEmpty(templateDto.getConfig().getSender()) ? DEFAULT_SENDER : templateDto.getConfig().getSender())
                .subject(ObjectUtils.isEmpty(templateDto.getConfig().getSubject()) ? existingDetails.getSubject() : templateDto.getConfig().getSubject())
                .toEmail(ObjectUtils.isEmpty(templateDto.getConfig().getToEmail()) ? getEmailIds(templateDto.getId()) : templateDto.getConfig().getToEmail())
                .build();

        TemplateDto updatedTemplateDto = TemplateDto.builder()
                .id(templateDto.getId())
                .data(ObjectUtils.isEmpty(templateDto.getData()) ? existingDetails.getBody() : templateDto.getData())
                .type("HTML")
                .config(config)
                .build();

        ActionDto action = ActionDto.builder()
                .template(updatedTemplateDto)
                .build();

        return NotificationDto.builder()
                .notificationId(UUID.randomUUID().toString())
                .type("email")
                .action(action)
                .build();
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
