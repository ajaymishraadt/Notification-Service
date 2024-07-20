package com.notification_service.notification_service.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "template", schema = "notification")
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "template_name", nullable = false, unique = true)
    private String templateName;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "html_body", nullable = false)
    private String htmlBody;
}
