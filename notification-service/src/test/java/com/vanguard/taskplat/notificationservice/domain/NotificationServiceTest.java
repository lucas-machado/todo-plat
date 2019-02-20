package com.vanguard.taskplat.notificationservice.domain;

import com.vanguard.taskplat.notificationservice.domain.exceptions.NotificationNotFoundException;
import com.vanguard.taskplat.notificationservice.domain.model.Notification;
import com.vanguard.taskplat.notificationservice.domain.repositories.NotificationRepository;
import com.vanguard.taskplat.notificationservice.domain.repositories.UserRepository;
import org.junit.Test;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NotificationServiceTest {

    private final NotificationRepository notificationRepository = mock(NotificationRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final NotificationService notificationService = new NotificationService(notificationRepository,
            userRepository);

    private final Long TASK_ID = 1L;
    private final Long USER_ID = 2L;
    private final String DESC_1 = "Desc 1";
    private final String DESC_2 = "Desc 2";
    private final LocalDateTime DATE_TIME = LocalDateTime.of(2019, 1, 1, 1, 0);
    private final Notification NOTIFICATION = new Notification(TASK_ID, USER_ID, DESC_1, DATE_TIME);

    @Test
    public void shouldCreateNotification() {
        notificationService.createNotification(TASK_ID, USER_ID, DESC_1, DATE_TIME);

        verify(notificationRepository).save(NOTIFICATION);
    }

    @Test
    public void shouldUpdateNotification() throws NotificationNotFoundException {
        when(notificationRepository.findById(TASK_ID)).thenReturn(Optional.of(NOTIFICATION));
        when(notificationRepository.save(NOTIFICATION)).thenReturn(NOTIFICATION);

        notificationService.updateNotification(TASK_ID, DESC_1, DATE_TIME);

        verify(notificationRepository).findById(TASK_ID);
        verify(notificationRepository).save(NOTIFICATION);
    }

    @Test(expected = NotificationNotFoundException.class)
    public void shouldThrowExceptionWhenUpdatingNonExistingNotification() throws NotificationNotFoundException {
        when(notificationRepository.findById(TASK_ID)).thenReturn(Optional.empty());

        notificationService.updateNotification(TASK_ID, DESC_1, DATE_TIME);
    }

    @Test
    public void shouldDeleteNotification() throws NotificationNotFoundException {
        notificationService.deleteNotification(TASK_ID);

        verify(notificationRepository).deleteById(TASK_ID);
    }

    @Test(expected = NotificationNotFoundException.class)
    public void shouldThrowExceptionWhenDeletingNonExistingNotification() throws NotificationNotFoundException {
        doThrow(new EmptyResultDataAccessException(1)).when(notificationRepository).deleteById(TASK_ID);

        notificationService.deleteNotification(TASK_ID);
    }
}
