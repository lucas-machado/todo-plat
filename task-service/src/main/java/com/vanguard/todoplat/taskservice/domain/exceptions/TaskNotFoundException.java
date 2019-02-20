package com.vanguard.todoplat.taskservice.domain.exceptions;

public class TaskNotFoundException extends Exception {

    public TaskNotFoundException(Long taskId) {
        super("Task " + taskId + " not found");
    }
}
