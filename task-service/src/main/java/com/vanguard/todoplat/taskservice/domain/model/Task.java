package com.vanguard.todoplat.taskservice.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Task {

    @Id
    @GeneratedValue
    private Long id;
    private Long userId;
    private String description;
    private LocalDateTime dateTime;


    public Task(Long userId, String description) {
        this.userId = userId;
        this.description = description;
    }

    public Task(Long userId, String description, LocalDateTime dateTime) {
        this.userId = userId;
        this.description = description;
        this.dateTime = dateTime;
    }
}