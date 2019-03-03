package com.vanguard.taskplat.notificationservice.application.domain.repositories;

import com.vanguard.taskplat.notificationservice.application.domain.model.Notification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends CrudRepository<Notification, Long> {
    List<Notification> findByUserId(Long userId);
}
