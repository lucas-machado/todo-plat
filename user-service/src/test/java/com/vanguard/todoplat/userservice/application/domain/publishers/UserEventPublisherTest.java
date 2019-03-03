package com.vanguard.todoplat.userservice.application.domain.publishers;

import com.vanguard.todoplat.userservice.api.events.UserCreatedEvent;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class UserEventPublisherTest {

    private final MessageChannel output = mock(MessageChannel.class);
    private final UserEventPublisher userEventPublisher = new UserEventPublisher(output);

    @Test
    public void shouldPublishUserCreatedEvent() {
        UserCreatedEvent userCreatedEvent = new UserCreatedEvent(1L, "username", "some@email.com");
        @SuppressWarnings("unchecked")
        ArgumentCaptor<Message<UserCreatedEvent>> argument = ArgumentCaptor.forClass(Message.class);

        userEventPublisher.publishUserCreated(userCreatedEvent);

        verify(output).send(argument.capture());
        Message<UserCreatedEvent> message = argument.getValue();
        assertEquals(message.getPayload(), userCreatedEvent);
    }
}
