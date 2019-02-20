package com.vanguard.taskplat.notificationservice.messaging;

import com.vanguard.taskplat.notificationservice.domain.NotificationService;
import com.vanguard.taskplat.notificationservice.domain.exceptions.UserNotFoundException;
import com.vanguard.todoplat.userservice.api.events.UserCreatedEvent;
import com.vanguard.todoplat.userservice.api.events.UserUpdatedEvent;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class NotificationServiceEventConsumerTest {

    private final NotificationService notificationService = mock(NotificationService.class);
    private final NotificationServiceEventConsumer eventConsumer =
            new NotificationServiceEventConsumer(notificationService);
    private static final long ID = 1L;
    private final String USERNAME = "username";
    private final String EMAIL = "some@email.com";

    @Test
    public void shouldProcessUserCreatedEvents() {
        UserCreatedEvent userCreatedEvent = new UserCreatedEvent(ID, USERNAME, EMAIL);

        eventConsumer.processUserCreatedEvent(userCreatedEvent);

        verify(notificationService).createUser(userCreatedEvent.getId(), userCreatedEvent.getUsername(),
                userCreatedEvent.getEmail());
    }

    @Test
    public void shouldProcessUserUpdatedEvents() throws UserNotFoundException {
        UserUpdatedEvent userUpdatedEvent = new UserUpdatedEvent(ID, USERNAME, EMAIL);

        eventConsumer.processUserUpdatedEvent(userUpdatedEvent);

        verify(notificationService).updateUser(userUpdatedEvent.getId(), userUpdatedEvent.getUsername(),
                userUpdatedEvent.getEmail());
    }
}
