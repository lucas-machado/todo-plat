package com.vanguard.todoplat.taskservice.messaging;

import com.vanguard.todoplat.taskservice.TaskServiceApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = TaskServiceApplication.class)
@WebAppConfiguration
@DirtiesContext
public class TaskServiceEventConsumerIntegrationTest {

    @Autowired
    private Sink sink;

    @Test
    public void contextLoads() {
        assertThat(sink.input()).isNotNull();
    }
}
