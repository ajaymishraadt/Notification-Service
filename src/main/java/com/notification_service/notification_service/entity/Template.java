package com.notification_service.notification_service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "Template", schema = "notification")
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "template_id", nullable = false, unique = true)
    private String templateId;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private String body;

    @ManyToMany(mappedBy = "templates")
    private Set<User> users = new HashSet<>();
}
