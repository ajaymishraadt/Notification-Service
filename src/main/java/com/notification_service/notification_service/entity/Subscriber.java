package com.notification_service.notification_service.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "subscriber", schema = "notification")
public class Subscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userName", nullable = false)
    private String userName;

    @Column(name = "emailId", nullable = false)
    private String emailId;

    @ManyToOne
    @JoinColumn(name = "templateId", referencedColumnName = "id")
    private Template template;
}
