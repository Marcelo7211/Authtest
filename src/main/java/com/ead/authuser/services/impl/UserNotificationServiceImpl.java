package com.ead.authuser.services.impl;

import com.ead.authuser.models.UserNotification;
import com.ead.authuser.repository.UserNotificationRepository;
import com.ead.authuser.services.UserNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserNotificationServiceImpl implements UserNotificationService {

    @Autowired
    private UserNotificationRepository repository;

    @Override
    public Page<UserNotification> findAll(Specification<UserNotification> spec, Pageable pageable) {
        return repository.findAll(spec, pageable);
    }

    @Override
    public List<UserNotification> findAllByDate(LocalDateTime date) {
        return repository.findAllByDate(date);
    }

    @Override
    public List<UserNotification> findAllByUserId(UUID userId) {
        return repository.findAllByUserUserId(userId);
    }

    @Override
    public UserNotification findById(UUID notificationId) {
        Optional<UserNotification> userNotification = repository.findById(notificationId);

        if(userNotification.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification not found with this uuid, try other uuid!");

        return userNotification.get();
    }

    @Override
    public UserNotification save(UserNotification userNotification) {
        return repository.save(userNotification);
    }

    @Override
    public UserNotification put(UUID notificationId, UserNotification userNotification) {
        var notification = this.findById(notificationId);
        userNotification.setNotificationId(notification.getNotificationId());
        return repository.save(userNotification);
    }

    @Override
    public void deleteById(UUID notificationId) {
       this.findById(notificationId);
        repository.deleteById(notificationId);
    }

    @Override
    public void deleteAll(List<UserNotification> notificationList) {
        if(notificationList.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "List is null or empty");

        for (UserNotification item: notificationList)
            this.findById(item.getNotificationId());

        repository.deleteAll(notificationList);
    }
}
