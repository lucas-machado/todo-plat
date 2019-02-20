package com.vanguard.todoplat.userservice.domain.exceptions;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(Long userId) {
        super("User " + userId + " not found");
    }
}
