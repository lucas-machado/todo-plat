package com.vanguard.taskplat.notificationservice.web;

import com.vanguard.taskplat.notificationservice.domain.NotificationService;
import com.vanguard.taskplat.notificationservice.domain.exceptions.NotificationNotFoundException;
import com.vanguard.todoplat.taskservice.api.events.TaskCreatedEvent;
import com.vanguard.todoplat.taskservice.api.events.TaskUpdatedEvent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/notifications/tasks")
public class NotificationController {
    private NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void handleTaskCreatedEvent(@Valid @RequestBody TaskCreatedEvent event) {
        notificationService.createNotification(event.getTaskId(), event.getUserId(), event.getDescription(),
                event.getDateTime());
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<Void> handleTaskUpdatedEvent(@PathVariable("taskId") Long taskId,
                                                       @Valid @RequestBody TaskUpdatedEvent event) {
        try {
            notificationService.updateNotification(taskId, event.getDescription(), event.getDateTime());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotificationNotFoundException notUsed) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> handleTaskDeletedEvent(@PathVariable("taskId") Long taskId) {
        try {
            notificationService.deleteNotification(taskId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NotificationNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}