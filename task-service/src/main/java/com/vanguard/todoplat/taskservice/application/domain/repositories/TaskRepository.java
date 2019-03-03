package com.vanguard.todoplat.taskservice.application.domain.repositories;

import com.vanguard.todoplat.taskservice.application.domain.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends PagingAndSortingRepository<Task, Long> {
    Page<Task> findByUserId(Long userId, Pageable pageable);
}
