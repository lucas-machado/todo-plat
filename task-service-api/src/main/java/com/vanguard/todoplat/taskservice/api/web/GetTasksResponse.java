package com.vanguard.todoplat.taskservice.api.web;


import lombok.Value;
import org.springframework.data.domain.Page;

@Value
public class GetTasksResponse {
    private final Page<TaskResponse> tasks;
}
