package com.vanguard.todoplat.taskservice.api.events;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class TaskUpdatedEvent {

    @NotNull
    @Size(min = 1, message = "Description should have at least 1 char")
    private final String description;

    private final LocalDateTime dateTime;
}
