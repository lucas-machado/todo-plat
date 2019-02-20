package com.vanguard.taskplat.notificationservice.messaging;

import com.vanguard.taskplat.notificationservice.domain.NotificationService;
import com.vanguard.taskplat.notificationservice.domain.exceptions.UserNotFoundException;
import com.vanguard.todoplat.userservice.api.events.UserCreatedEvent;
import com.vanguard.todoplat.userservice.api.events.UserUpdatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

@EnableBinding(Sink.class)
@Slf4j
public class NotificationServiceEventConsumer {
    private final NotificationService notificationService;

    public NotificationServiceEventConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @StreamListener(value = Sink.INPUT,
            condition = "headers['type']=='UserCreatedEvent'")
    public void processUserCreatedEvent(UserCreatedEvent event) {
        log.info("processing {}", event);
        notificationService.createUser(event.getId(), event.getUsername(), event.getEmail());
    }

    @StreamListener(value = Sink.INPUT,
            condition = "headers['type']=='UserUpdatedEvent'")
    public void processUserUpdatedEvent(UserUpdatedEvent event) throws UserNotFoundException {
        log.info("processing {}", event);
        notificationService.updateUser(event.getId(), event.getUsername(), event.getEmail());
    }
}
