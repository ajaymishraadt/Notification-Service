package com.notification_service.notification_service.repository;

import com.notification_service.notification_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u JOIN u.templates t WHERE t.templateId = :templateId")
    List<User> findAllByTemplatesTemplateId(@Param("templateId") String templateId);
}