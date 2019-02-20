package com.vanguard.todoplat.taskservice.domain;

import com.vanguard.todoplat.taskservice.api.events.TaskCreatedEvent;
import com.vanguard.todoplat.taskservice.api.events.TaskUpdatedEvent;
import com.vanguard.todoplat.taskservice.domain.exceptions.TaskNotFoundException;
import com.vanguard.todoplat.taskservice.domain.model.Task;
import com.vanguard.todoplat.taskservice.domain.proxies.NotificationServiceProxy;
import com.vanguard.todoplat.taskservice.domain.repositories.TaskRepository;
import org.junit.Test;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TaskServiceTest {

    private final TaskRepository taskRepository = mock(TaskRepository.class);
    private final NotificationServiceProxy notificationService = mock(NotificationServiceProxy.class);
    private final TaskService taskService = new TaskService(taskRepository, notificationService);
    private final Long USER_ID = 1L;
    private final String TASK_DESCRIPTION_1 = "Task Description 1";
    private final String TASK_DESCRIPTION_2 = "Task Description 2";
    private final LocalDateTime TASK_DATETIME = LocalDateTime.of(2019, 1, 1, 1, 0);
    private final Task TASK_1 = new Task(USER_ID, TASK_DESCRIPTION_1, TASK_DATETIME);
    private final Task TASK_2 = new Task(USER_ID, TASK_DESCRIPTION_2);
    private final Task CREATED_TASK_1 = new Task(1L, TASK_1.getUserId(), TASK_1.getDescription(),
            TASK_1.getDateTime());

    @Test
    public void shouldAllowToCreateTasks() {
        when(taskRepository.save(TASK_1)).thenReturn(CREATED_TASK_1);

        Task createdTask = taskService.createTask(TASK_1.getUserId(), TASK_1.getDescription(), TASK_1.getDateTime());

        assertNotNull(createdTask);
        assertEquals(CREATED_TASK_1, createdTask);
        verify(taskRepository).save(TASK_1);
        verify(notificationService).taskCreated(new TaskCreatedEvent(createdTask.getId(), createdTask.getUserId(),
                createdTask.getDescription(), createdTask.getDateTime()));
    }

    @Test
    public void shouldAllowToRetrieveTasks() {
        when(taskRepository.findByUserId(USER_ID)).thenReturn(asList(TASK_1, TASK_2));

        List<Task> tasks = taskService.getTasks(USER_ID);

        assertTrue(tasks.containsAll(asList(TASK_1, TASK_2)));
    }

    @Test
    public void shouldAllowToUpdateATask() throws TaskNotFoundException {
        when(taskRepository.findById(CREATED_TASK_1.getId())).thenReturn(Optional.of(CREATED_TASK_1));
        when(taskRepository.save(CREATED_TASK_1)).thenReturn(CREATED_TASK_1);

        taskService.updateTask(CREATED_TASK_1.getId(), CREATED_TASK_1.getDescription(), CREATED_TASK_1.getDateTime());

        verify(taskRepository).findById(CREATED_TASK_1.getId());
        verify(notificationService).taskUpdated(CREATED_TASK_1.getId(),
                new TaskUpdatedEvent(CREATED_TASK_1.getDescription(), CREATED_TASK_1.getDateTime()));
    }

    @Test
    public void shouldAllowToDeleteATask() throws TaskNotFoundException {
        taskService.deleteTask(CREATED_TASK_1.getId());

        verify(taskRepository).deleteById(CREATED_TASK_1.getId());
        verify(notificationService).taskDeleted(CREATED_TASK_1.getId());
    }

    @Test
    public void shouldThrowExceptionForDeleteOfNonExistingTask() throws TaskNotFoundException {
        final int expectedResultSize = 1;
        doThrow(new EmptyResultDataAccessException(expectedResultSize)).when(taskRepository).deleteById(TASK_1.getId());

        catchException(taskService).deleteTask(TASK_1.getId());

        assertTrue(caughtException() instanceof TaskNotFoundException);
        assertEquals("Task " + TASK_1.getId() + " not found", caughtException().getMessage());
    }

    @Test
    public void shouldSayHelloToUser() {
        final String USERNAME = "Username";
        final String EMAIL = "some@email.com";

        taskService.sayHello(USER_ID, USERNAME, EMAIL);
    }
}
