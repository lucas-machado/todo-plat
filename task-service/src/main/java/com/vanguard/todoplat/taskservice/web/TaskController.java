package com.vanguard.todoplat.taskservice.web;

import com.vanguard.todoplat.taskservice.TaskServiceProperties;
import com.vanguard.todoplat.taskservice.api.web.CreateTaskRequest;
import com.vanguard.todoplat.taskservice.api.web.CreateTaskResponse;
import com.vanguard.todoplat.taskservice.api.web.GetTasksResponse;
import com.vanguard.todoplat.taskservice.api.web.TaskResponse;
import com.vanguard.todoplat.taskservice.api.web.UpdateTaskRequest;
import com.vanguard.todoplat.taskservice.application.TaskService;
import com.vanguard.todoplat.taskservice.application.domain.exceptions.TaskNotFoundException;
import com.vanguard.todoplat.taskservice.application.domain.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskServiceProperties serviceProperties;

    public TaskController(TaskService taskService, TaskServiceProperties serviceProperties) {
        this.taskService = taskService;
        this.serviceProperties = serviceProperties;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateTaskResponse create(@Valid @RequestBody CreateTaskRequest request) {
        Task task = taskService.createTask(request.getUserId(), request.getDescription(), request.getDateTime());
        return new CreateTaskResponse(task.getId());
    }

    @GetMapping("/{userId}")
    public GetTasksResponse getTasks(@PathVariable("userId") Long userId,
                                     Pageable pageRequest) {
        if(pageRequest.getPageSize() > serviceProperties.getMaxPageSize()) {
            pageRequest = PageRequest.of(pageRequest.getPageNumber(), serviceProperties.getMaxPageSize(),
                    pageRequest.getSort());
        }

        Page<TaskResponse> tasks = convertToTaskResponseList(taskService.getTasks(userId, pageRequest));
        return new GetTasksResponse(tasks);
    }

    private Page<TaskResponse> convertToTaskResponseList(Page<Task> tasks) {
        List<TaskResponse> taskResponses = tasks.getContent().stream()
                    .map(task -> new TaskResponse(task.getId(), task.getDescription()))
                    .collect(Collectors.toList());

        return new PageImpl<>(taskResponses, tasks.getPageable(), tasks.getTotalElements());
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<Void> update(@PathVariable("taskId") Long taskId,
                               @Valid @RequestBody UpdateTaskRequest request) {
        try {
            taskService.updateTask(taskId, request.getDescription(), request.getDateTime());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (TaskNotFoundException notUsed) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> delete(@PathVariable("taskId") Long taskId) {
        try {
            taskService.deleteTask(taskId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (TaskNotFoundException notUsed) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
