package com.vanguard.todoplat.taskservice.application.domain.exceptions;

public class TaskNotFoundException extends Exception {

    public TaskNotFoundException(Long taskId) {
        super("Task " + taskId + " not found");
    }
}
