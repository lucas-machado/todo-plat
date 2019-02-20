package com.vanguard.todoplat.taskservice.api.web;


import lombok.Value;

import java.util.List;

@Value
public class GetTasksResponse {
    private final List<TaskResponse> tasks;
}
