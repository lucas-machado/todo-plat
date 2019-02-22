package com.vanguard.todoplat.taskservice.domain;

import com.vanguard.todoplat.taskservice.api.events.TaskCreatedEvent;
import com.vanguard.todoplat.taskservice.domain.exceptions.TaskNotFoundException;
import com.vanguard.todoplat.taskservice.domain.model.Task;
import com.vanguard.todoplat.taskservice.domain.proxies.NotificationServiceProxy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class TaskServiceIntegrationTest {

    @MockBean
    private NotificationServiceProxy notificationService;

    @Autowired
    private TaskService taskService;

    private final Long USER_ID_1 = 1L;
    private final String DESCRIPTION_1 = "Desc 1";
    private final String DESCRIPTION_2 = "Desc 2";
    private final LocalDateTime DATETIME_1 = LocalDateTime.of(2019, 1, 1, 1, 0);
    private final LocalDateTime DATETIME_2 = LocalDateTime.of(2019, 2, 1, 1, 0);
    private final Pageable PAGE_REQUEST = PageRequest.of(0, 20);

    @Test
    public void shouldCreateTask() {
        Task task = taskService.createTask(USER_ID_1, DESCRIPTION_1, DATETIME_1);
        assertNotNull(task);
        assertNotNull(task.getId());
        assertEquals(task.getUserId(), USER_ID_1);
        assertEquals(task.getDescription(), DESCRIPTION_1);
        assertEquals(task.getDateTime(), DATETIME_1);
        verify(notificationService).taskCreated(any(TaskCreatedEvent.class));
    }

    @Test
    public void shouldCreateManyTasks() {
        Task task1 = taskService.createTask(USER_ID_1, DESCRIPTION_1, DATETIME_1);
        Task task2 = taskService.createTask(USER_ID_1, DESCRIPTION_2, DATETIME_2);
        Page<Task> tasks = taskService.getTasks(USER_ID_1, PAGE_REQUEST);

        assertEquals(2, tasks.getTotalElements());
        assertTrue(taskService.getTasks(USER_ID_1, PAGE_REQUEST).getContent().containsAll(asList(task1, task2)));
    }

    @Test
    public void shouldUpdateTask() throws TaskNotFoundException {
        Task task = taskService.createTask(USER_ID_1, DESCRIPTION_1, DATETIME_1);

        Task updatedTask = taskService.updateTask(task.getId(), DESCRIPTION_2, task.getDateTime());

        assertEquals(updatedTask.getDescription(), DESCRIPTION_2);
    }

    @Test
    public void shouldDeleteTask() throws TaskNotFoundException {
        Task task = taskService.createTask(USER_ID_1, DESCRIPTION_1, DATETIME_1);

        taskService.deleteTask(task.getId());

        assertTrue(taskService.getTasks(USER_ID_1, PAGE_REQUEST).isEmpty());
    }

    @Test(expected = TaskNotFoundException.class)
    public void shouldThrowExceptionForDeleteNonExitingTask() throws TaskNotFoundException {
        taskService.deleteTask(0L);
    }
}
