package com.vanguard.todoplat.taskservice.domain;

import com.vanguard.todoplat.taskservice.api.events.TaskCreatedEvent;
import com.vanguard.todoplat.taskservice.api.events.TaskUpdatedEvent;
import com.vanguard.todoplat.taskservice.domain.exceptions.TaskNotFoundException;
import com.vanguard.todoplat.taskservice.domain.model.Task;
import com.vanguard.todoplat.taskservice.domain.proxies.NotificationServiceProxy;
import com.vanguard.todoplat.taskservice.domain.repositories.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class TaskService {
    private final TaskRepository taskRepository;
    private NotificationServiceProxy notificationService;

    public TaskService(TaskRepository taskRepository, NotificationServiceProxy notificationService) {
        this.taskRepository = taskRepository;
        this.notificationService = notificationService;
    }

    public Task createTask(long userId, String taskDescription, LocalDateTime dateTime) {
        Task task = new Task(userId, taskDescription, dateTime);
        Task saved = taskRepository.save(task);
        notificationService.taskCreated(new TaskCreatedEvent(saved.getId(), userId, taskDescription, dateTime));
        return saved;
    }

    public Page<Task> getTasks(Long userId, Pageable pageRequest) {
        return taskRepository.findByUserId(userId, pageRequest);
    }

    public Task updateTask(Long taskId, String description, LocalDateTime dateTime) throws TaskNotFoundException {
        return taskRepository.findById(taskId).map(task -> {
            task.setDescription(description);
            task.setDateTime(dateTime);
            Task updatedTask = taskRepository.save(task);
            notificationService.taskUpdated(taskId, new TaskUpdatedEvent(description, dateTime));
            return updatedTask;
        }).orElseThrow(() -> new TaskNotFoundException(taskId));
    }

    public void deleteTask(Long taskId) throws TaskNotFoundException {
        try {
            taskRepository.deleteById(taskId);
            notificationService.taskDeleted(taskId);
        } catch (EmptyResultDataAccessException notUsed) {
            throw new TaskNotFoundException(taskId);
        }
    }

    public void sayHello(Long userId, String username, String email) {
        log.info("Hello userId:" + userId + " username:" + username + " email:" + email);
    }
}
