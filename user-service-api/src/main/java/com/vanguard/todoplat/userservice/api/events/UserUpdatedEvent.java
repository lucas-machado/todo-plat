package com.vanguard.todoplat.userservice.api.events;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UserUpdatedEvent {

    private final Long id;
    private final String username;
    private final String email;
}
