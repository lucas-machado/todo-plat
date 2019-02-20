package com.vanguard.todoplat.taskservice.domain.repositories;

import com.vanguard.todoplat.taskservice.domain.model.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {
    List<Task> findByUserId(Long userId);
}
