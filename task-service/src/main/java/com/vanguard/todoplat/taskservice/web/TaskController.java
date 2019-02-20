package com.vanguard.todoplat.taskservice.web;

import com.vanguard.todoplat.taskservice.api.web.CreateTaskRequest;
import com.vanguard.todoplat.taskservice.api.web.CreateTaskResponse;
import com.vanguard.todoplat.taskservice.api.web.GetTasksResponse;
import com.vanguard.todoplat.taskservice.api.web.TaskResponse;
import com.vanguard.todoplat.taskservice.api.web.UpdateTaskRequest;
import com.vanguard.todoplat.taskservice.domain.TaskService;
import com.vanguard.todoplat.taskservice.domain.exceptions.TaskNotFoundException;
import com.vanguard.todoplat.taskservice.domain.model.Task;
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

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateTaskResponse create(@Valid @RequestBody CreateTaskRequest request) {
        Task task = taskService.createTask(request.getUserId(), request.getDescription(), request.getDateTime());
        return new CreateTaskResponse(task.getId());
    }

    @GetMapping("/{userId}")
    public GetTasksResponse getTasks(@PathVariable("userId") Long userId) {
        List<TaskResponse> tasks = convertToTaskResponseList(taskService.getTasks(userId));
        return new GetTasksResponse(tasks);
    }

    private List<TaskResponse> convertToTaskResponseList(List<Task> tasks) {
        return tasks.stream()
                    .map(task -> new TaskResponse(task.getId(), task.getDescription()))
                    .collect(Collectors.toList());
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
