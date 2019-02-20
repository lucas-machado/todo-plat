package com.vanguard.todoplat.taskservice.api.web;

import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
public class CreateTaskResponse {

    @NotNull
    private final Long taskId;
}
