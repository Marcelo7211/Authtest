package com.ead.authuser.repository;

import com.ead.authuser.models.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, UUID>, JpaSpecificationExecutor<UserNotification> {
    List<UserNotification> findAllByDate (@Param("date") LocalDateTime date);
    List<UserNotification> findAllByUserUserId(@Param("userId") UUID userId);
}
