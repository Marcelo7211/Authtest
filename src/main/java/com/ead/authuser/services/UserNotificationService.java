package com.ead.authuser.services;

import com.ead.authuser.models.UserNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface UserNotificationService {
    Page<UserNotification> findAll(Specification<UserNotification> spec,  Pageable pageable);
    List<UserNotification> findAllByDate(LocalDateTime date);
    List<UserNotification> findAllByUserId(UUID userId);
    UserNotification findById(UUID notificationId);
    UserNotification save(UserNotification userNotification);
    UserNotification put(UUID notificationId, UserNotification userNotification);
    void deleteById (UUID notificationId);
    void deleteAll(List<UserNotification> notificationList);
}
