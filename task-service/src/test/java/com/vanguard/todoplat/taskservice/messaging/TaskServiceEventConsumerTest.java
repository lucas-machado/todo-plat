package com.vanguard.todoplat.taskservice.messaging;

import com.vanguard.todoplat.taskservice.domain.TaskService;
import com.vanguard.todoplat.userservice.api.events.UserCreatedEvent;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TaskServiceEventConsumerTest {

    private final TaskService taskService = mock(TaskService.class);
    private final TaskServiceEventConsumer eventConsumer = new TaskServiceEventConsumer(taskService);

    @Test
    public void shouldConsumeUserCreatedEvent() {
        final long id = 1L;
        final String username = "username";
        final String email = "some@email.com";
        UserCreatedEvent userCreatedEvent = new UserCreatedEvent(id, username, email);

        eventConsumer.processUserCreatedEvent(userCreatedEvent);

        verify(taskService).sayHello(id, username, email);
    }
}
