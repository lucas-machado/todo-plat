package com.vanguard.taskplat.notificationservice.web;

import com.vanguard.taskplat.notificationservice.domain.NotificationService;
import com.vanguard.taskplat.notificationservice.domain.exceptions.NotificationNotFoundException;
import com.vanguard.todoplat.taskservice.api.events.TaskCreatedEvent;
import com.vanguard.todoplat.taskservice.api.events.TaskUpdatedEvent;
import org.junit.Test;

import java.time.LocalDateTime;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NotificationControllerTest {

    private final NotificationService notificationService = mock(NotificationService.class);
    private final NotificationController notificationController = new NotificationController(notificationService);
    private final Long TASK_ID = 1L;
    private final Long USER_ID = 1L;
    private final String DESCRIPTION = "Some desc...";
    private final LocalDateTime DATETIME = LocalDateTime.of(2019, 1, 1, 1, 0);

    @Test
    public void shouldAcceptTaskCreatedEvent() {
        TaskCreatedEvent taskCreatedEvent = new TaskCreatedEvent(TASK_ID, USER_ID, DESCRIPTION, DATETIME);

        given().
                standaloneSetup(notificationController).
                contentType("application/json").
                body(taskCreatedEvent).
        when().
                post("/notifications/tasks").
        then().
                statusCode(201);
    }

    @Test
    public void shouldAcceptTaskUpdatedEvent() {
        TaskUpdatedEvent taskUpdatedEvent = new TaskUpdatedEvent(DESCRIPTION, DATETIME);

        given().
                standaloneSetup(notificationController).
                contentType("application/json").
                body(taskUpdatedEvent).
        when().
                put("/notifications/tasks/{taskId}", TASK_ID).
        then().
                statusCode(200);
    }

    @Test
    public void shouldFailUpdatingNonExistingNotification() throws NotificationNotFoundException {
        when(notificationService.updateNotification(TASK_ID, DESCRIPTION, DATETIME))
                .thenThrow(new NotificationNotFoundException(TASK_ID));

        TaskUpdatedEvent taskUpdatedEvent = new TaskUpdatedEvent(DESCRIPTION, DATETIME);

        given().
                standaloneSetup(notificationController).
                contentType("application/json").
                body(taskUpdatedEvent).
        when().
                put("/notifications/tasks/{taskId}", TASK_ID).
        then().
                statusCode(404);
    }

    @Test
    public void shouldDeleteNotification() {
        given().
                standaloneSetup(notificationController).
        when().
                delete("/notifications/tasks/{taskId}", TASK_ID).
        then().
                statusCode(204);
    }
}
