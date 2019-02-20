package com.vanguard.todoplat.taskservice.messaging;

import com.vanguard.todoplat.taskservice.domain.TaskService;
import com.vanguard.todoplat.userservice.api.events.UserCreatedEvent;
import com.vanguard.todoplat.userservice.api.events.UserUpdatedEvent;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(Sink.class)
public class TaskServiceEventConsumer {

    private final TaskService taskService;

    public TaskServiceEventConsumer(TaskService taskService) {
        this.taskService = taskService;
    }

    @StreamListener(value = Sink.INPUT, condition = "headers['type']=='UserCreatedEvent'")
    public void processUserCreatedEvent(UserCreatedEvent event) {
        taskService.sayHello(event.getId(), event.getUsername(), event.getEmail());
    }

    @StreamListener(value = Sink.INPUT, condition = "headers['type']=='UserUpdatedEvent'")
    public void processUserUpdatedEvent(UserUpdatedEvent event) {
        //do nothing, no interest on this event
    }
}
