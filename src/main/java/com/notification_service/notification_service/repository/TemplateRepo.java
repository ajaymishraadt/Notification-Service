package com.notification_service.notification_service.repository;

import com.notification_service.notification_service.dto.TemplateDetailsDto;
import com.notification_service.notification_service.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface TemplateRepo extends JpaRepository<Template,Long> {
    @Query(value = "SELECT t.subject, t.html_body, " +
            "(SELECT array_agg(s.email_id) FROM subscriber s WHERE s.template_id = t.id) " +
            "FROM template t WHERE t.template_name = :templateName",
            nativeQuery = true)
    TemplateDetailsDto findTemplateDetailsByTemplateName(@Param("templateName") String templateName);
}
