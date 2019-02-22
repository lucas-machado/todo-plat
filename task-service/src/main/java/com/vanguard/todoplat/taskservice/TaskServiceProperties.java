package com.vanguard.todoplat.taskservice;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Component
@ConfigurationProperties(prefix = "todoplat.tasks")
@Data
@Validated
public class TaskServiceProperties {

    @Min(value = 1)
    @Max(value = 10)
    private int maxPageSize = 10;
}