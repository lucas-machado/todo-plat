package com.vanguard.todoplat.userservice.application.domain.exceptions;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(Long userId) {
        super("User " + userId + " not found");
    }
}
