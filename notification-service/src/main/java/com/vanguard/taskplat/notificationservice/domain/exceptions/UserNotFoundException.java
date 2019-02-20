package com.vanguard.taskplat.notificationservice.domain.exceptions;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(Long id) {
        super("User " + id +  " not found");
    }
}
