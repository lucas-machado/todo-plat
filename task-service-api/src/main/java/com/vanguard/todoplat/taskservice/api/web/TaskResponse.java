package com.vanguard.todoplat.taskservice.api.web;

import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
public class TaskResponse {

    @NotNull
    private final Long taskId;

    @NotNull
    private final String description;
}
