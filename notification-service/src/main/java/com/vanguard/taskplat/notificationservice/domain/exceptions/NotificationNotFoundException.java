package com.vanguard.taskplat.notificationservice.domain.exceptions;

public class NotificationNotFoundException extends Exception {
    public NotificationNotFoundException(Long taskId) {
        super("Notification of task " + taskId + " not found");
    }
}
