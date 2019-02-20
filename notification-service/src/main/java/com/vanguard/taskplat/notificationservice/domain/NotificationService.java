package com.vanguard.taskplat.notificationservice.domain;

import com.vanguard.taskplat.notificationservice.domain.exceptions.NotificationNotFoundException;
import com.vanguard.taskplat.notificationservice.domain.exceptions.UserNotFoundException;
import com.vanguard.taskplat.notificationservice.domain.model.Notification;
import com.vanguard.taskplat.notificationservice.domain.model.User;
import com.vanguard.taskplat.notificationservice.domain.repositories.NotificationRepository;
import com.vanguard.taskplat.notificationservice.domain.repositories.UserRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    public Notification createNotification(Long taskId, Long userId, String description, LocalDateTime dateTime) {
        return notificationRepository.save(new Notification(taskId, userId, description, dateTime));
    }

    public Notification updateNotification(Long taskId, String description, LocalDateTime dateTime)
            throws NotificationNotFoundException {
        return notificationRepository.findById(taskId).map(notification -> {
            notification.setDescription(description);
            notification.setDateTime(dateTime);
            return notificationRepository.save(notification);
        }).orElseThrow(() -> new NotificationNotFoundException(taskId));
    }

    public void deleteNotification(Long taskId) throws NotificationNotFoundException {
        try {
            notificationRepository.deleteById(taskId);
        } catch (EmptyResultDataAccessException notUsed) {
            throw new NotificationNotFoundException(taskId);
        }
    }

    public List<Notification> getNotifications(Long userId) {
        return notificationRepository.findByUserId(userId);
    }

    public User createUser(Long id, String username, String email) {
        User user = new User(id, username, email);
        return userRepository.save(user);
    }

    public User updateUser(Long id, String username, String email) throws UserNotFoundException {
        return userRepository.findById(id).map(user -> {
            user.setUsername(username);
            user.setEmail(email);
            return userRepository.save(user);
        }).orElseThrow(() -> new UserNotFoundException(id));
    }
}
