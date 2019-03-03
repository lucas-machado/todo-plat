package com.vanguard.taskplat.notificationservice.application.domain.exceptions;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(Long id) {
        super("User " + id +  " not found");
    }
}
