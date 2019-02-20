package com.vanguard.todoplat.userservice.api.web;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CreateUserRequest {

    @NotNull
    @Size(min = 8)
    private final String username;

    @NotNull
    @Size(min = 8)
    private final String password;

    @NotNull
    @Email
    private final String email;
}
