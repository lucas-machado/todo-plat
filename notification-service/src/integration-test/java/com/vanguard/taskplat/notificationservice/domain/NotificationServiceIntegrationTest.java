package com.vanguard.taskplat.notificationservice.domain;

import com.vanguard.taskplat.notificationservice.domain.exceptions.NotificationNotFoundException;
import com.vanguard.taskplat.notificationservice.domain.model.Notification;
import com.vanguard.taskplat.notificationservice.domain.repositories.NotificationRepository;
import com.vanguard.taskplat.notificationservice.domain.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class NotificationServiceIntegrationTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    private NotificationService notificationService;

    private static final Long USER_ID = 1L;

    private static final Long TASK_ID_1 = 2L;
    private static final String DESCRIPTON_1 = "Desc...";
    private static final LocalDateTime DATE_TIME_1 = LocalDateTime.of(2019, 1, 1, 1, 0);

    private static final Long TASK_ID_2 = 3L;
    private static final String DESCRIPTON_2 = "Desc 2...";
    private static final LocalDateTime DATE_TIME_2 = LocalDateTime.of(2020, 1, 1, 1, 0);

    @Before
    public void setUp() {
        notificationService = new NotificationService(notificationRepository, userRepository);
    }

    @Test
    public void shouldCreateNotification() {
        Notification notification = notificationService.createNotification(TASK_ID_1, USER_ID, DESCRIPTON_1,
                DATE_TIME_1);

        assertNotNull(notification);
        assertEquals(TASK_ID_1, notification.getTaskId());
        assertEquals(USER_ID, notification.getUserId());
        assertEquals(DESCRIPTON_1, notification.getDescription());
        assertEquals(DATE_TIME_1, notification.getDateTime());
    }

    @Test
    public void shouldCreateManyNotifications() {
        Notification notification1 = notificationService.createNotification(TASK_ID_1, USER_ID, DESCRIPTON_1,
                DATE_TIME_1);
        Notification notification2 = notificationService.createNotification(TASK_ID_2, USER_ID, DESCRIPTON_2,
                DATE_TIME_2);

        List<Notification> notifications = notificationService.getNotifications(USER_ID);

        assertEquals(2, notifications.size());
        assertTrue(notifications.containsAll(asList(notification1, notification2)));
    }

    @Test
    public void shouldUpdateNotification() throws NotificationNotFoundException {
        notificationService.createNotification(TASK_ID_1, USER_ID, DESCRIPTON_1, DATE_TIME_1);

        Notification updatedNotification = notificationService.updateNotification(TASK_ID_1, DESCRIPTON_2, DATE_TIME_1);

        assertEquals(DESCRIPTON_2, updatedNotification.getDescription());
    }

    @Test(expected = NotificationNotFoundException.class)
    public void shouldThrowExceptionWhenUpdatingNonExistingNotification() throws NotificationNotFoundException {
        notificationService.updateNotification(TASK_ID_1, DESCRIPTON_1, DATE_TIME_1);
    }

    @Test
    public void shouldDeleteNotification() throws NotificationNotFoundException {
        notificationService.createNotification(TASK_ID_1, USER_ID, DESCRIPTON_1,
                DATE_TIME_1);

        notificationService.deleteNotification(TASK_ID_1);

        assertTrue(notificationService.getNotifications(USER_ID).isEmpty());
    }

    @Test(expected = NotificationNotFoundException.class)
    public void shouldThrowExceptionWhenDeletingNonExistingNotification() throws NotificationNotFoundException {
        notificationService.deleteNotification(TASK_ID_1);
    }
}
