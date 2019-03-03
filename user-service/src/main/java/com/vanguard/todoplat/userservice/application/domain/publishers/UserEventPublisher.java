package com.vanguard.todoplat.userservice.application.domain.publishers;

import com.vanguard.todoplat.userservice.api.events.UserCreatedEvent;
import com.vanguard.todoplat.userservice.api.events.UserUpdatedEvent;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(Source.class)
public class UserEventPublisher {
    private final MessageChannel output;

    public UserEventPublisher(MessageChannel output) {
        this.output = output;
    }

    public void publishUserCreated(UserCreatedEvent event) {
        output.send(buildMessage(event));
    }

    public void publishUserUpdated(UserUpdatedEvent event) {
        output.send(buildMessage(event));
    }

    private <T> Message<T> buildMessage(T payload) {
        return MessageBuilder.withPayload(payload)
                       .setHeader("type", payload.getClass().getSimpleName())
                       .build();
    }
}
