package com.vanguard.taskplat.notificationservice.application.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Notification {
    @Id
    private Long taskId;

    @NotNull
    private Long userId;

    @NotNull
    @Size(min = 1, message = "Descriptions should have at least 1 char")
    private String description;

    private LocalDateTime dateTime;
}
