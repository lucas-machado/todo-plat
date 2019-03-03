package com.vanguard.todoplat.taskservice.web;

import com.vanguard.todoplat.taskservice.TaskServiceProperties;
import com.vanguard.todoplat.taskservice.api.web.CreateTaskRequest;
import com.vanguard.todoplat.taskservice.api.web.UpdateTaskRequest;
import com.vanguard.todoplat.taskservice.application.TaskService;
import com.vanguard.todoplat.taskservice.application.domain.exceptions.TaskNotFoundException;
import com.vanguard.todoplat.taskservice.application.domain.model.Task;
import org.junit.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TaskControllerTest {

    private final TaskService taskService = mock(TaskService.class);
    private final TaskServiceProperties serviceProperties = new TaskServiceProperties();
    private final TaskController taskController = new TaskController(taskService, serviceProperties);
    private final Long USER_ID = 12345L;
    private final String TASK_DESCRIPTION = "abcd";
    private final Long TASK_ID = 1L;
    private final LocalDateTime TASK_DATETIME = LocalDateTime.of(2019, 1, 1, 1, 0);
    private Task task = new Task(TASK_ID, USER_ID, TASK_DESCRIPTION, TASK_DATETIME);
    private final Pageable PAGE_REQUEST = PageRequest.of(0, 20);

    @Test
    public void shouldCreateTask() {
        when(taskService.createTask(USER_ID, TASK_DESCRIPTION, TASK_DATETIME)).thenReturn(task);
        CreateTaskRequest createTaskRequest = new CreateTaskRequest(USER_ID, TASK_DESCRIPTION, TASK_DATETIME);

        given().
                       standaloneSetup(taskController).
                       contentType("application/json").
                       body(createTaskRequest).
        when().
                       post("/tasks").
        then().
                       statusCode(201).
                       body("taskId", equalTo(TASK_ID.intValue()));
    }

    @Test
    public void shouldNotAllowCreatingTaskWithEmptyDescription() {
        CreateTaskRequest createTaskRequest = new CreateTaskRequest(USER_ID, "", null);

        given().
                       standaloneSetup(taskController).
                       contentType("application/json").
                       body(createTaskRequest).
        when().
                       post("/tasks").
        then().
                       statusCode(400);
    }

    @Test
    public void shouldGetUserTasks() {
        when(taskService.getTasks(USER_ID, PAGE_REQUEST)).thenReturn(new PageImpl<>(singletonList(task)));

        given().
                       standaloneSetup(taskController).
        when().
                       get("/tasks/{userID}", USER_ID).
        then().
                       statusCode(200).
                       body("tasks.taskId", hasItem(task.getId().intValue())).
                       body("tasks.description", hasItem(task.getDescription()));
    }

    @Test
    public void shouldUpdateTask() throws TaskNotFoundException {
        when(taskService.updateTask(task.getId(), task.getDescription(), task.getDateTime()))
                .thenReturn(task);

        UpdateTaskRequest updateTaskRequest = new UpdateTaskRequest(task.getDescription(), task.getDateTime());

        given().
                       standaloneSetup(taskController).
                       contentType("application/json").
                       body(updateTaskRequest).
        when().
                       put("/tasks/{taskId}", task.getId()).
        then().
                       statusCode(200);
    }

    @Test
    public void shouldDeleteTask() {
        given().
                       standaloneSetup(taskController).
        when().
                       delete("/tasks/{taskId}", task.getId()).
        then().
                       statusCode(204);
    }

    @Test
    public void shouldReceiveNotFoundForDeleteOfNonExistingTask() throws TaskNotFoundException {
        doThrow(new TaskNotFoundException(task.getId())).when(taskService).deleteTask(task.getId());

        given().
                       standaloneSetup(taskController).
        when().
                       delete("/tasks/{taskId}", task.getId()).
        then().
                       statusCode(404);
    }
}
