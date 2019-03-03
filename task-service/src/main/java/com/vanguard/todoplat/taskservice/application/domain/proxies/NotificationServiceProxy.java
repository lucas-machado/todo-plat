package com.vanguard.todoplat.taskservice.application.domain.proxies;

import com.vanguard.todoplat.taskservice.api.events.TaskCreatedEvent;
import com.vanguard.todoplat.taskservice.api.events.TaskUpdatedEvent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(url = "http://localhost:8081", name = "notification")
public interface NotificationServiceProxy {

    @PostMapping("/events/tasks")
    void taskCreated(TaskCreatedEvent event);

    @PutMapping("/events/tasks/{taskId}")
    void taskUpdated(@PathVariable("taskId") Long taskId, TaskUpdatedEvent event);

    @DeleteMapping("/events/tasks/{taskId}")
    void taskDeleted(@PathVariable("taskId") Long taskId);
}
