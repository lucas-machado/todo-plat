package com.vanguard.todoplat.userservice.application;

import com.vanguard.todoplat.userservice.api.events.UserCreatedEvent;
import com.vanguard.todoplat.userservice.api.events.UserUpdatedEvent;
import com.vanguard.todoplat.userservice.application.domain.exceptions.UserNotFoundException;
import com.vanguard.todoplat.userservice.application.domain.model.User;
import com.vanguard.todoplat.userservice.application.domain.publishers.UserEventPublisher;
import com.vanguard.todoplat.userservice.application.domain.repositories.UserRepository;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnitParamsRunner.class)
public class UserServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserEventPublisher userEventPublisher = mock(UserEventPublisher.class);
    private final UserService userService = new UserService(userRepository, userEventPublisher);

    @Test
    @Parameters({"validusername, validpassword, valid@email.com"})
    public void shouldCreateValidUser(String validUsername, String validPassword, String validEmail) {
        final User user = new User(validUsername, validPassword, validEmail);
        final User userWithId = new User(1L, validUsername, validPassword, validEmail);

        when(userRepository.save(user)).thenReturn(userWithId);

        userService.createUser(validUsername, validPassword, validEmail);

        verify(userRepository).save(user);
        verify(userEventPublisher).publishUserCreated(new UserCreatedEvent(userWithId.getId(), userWithId.getUsername(),
                userWithId.getEmail()));
    }

    @Test
    public void shouldRejectInvalidUser() {

    }

    @Test
    public void shouldUpdateUser() throws UserNotFoundException {
        final long id = 1L;
        final String username = "username";
        final String password = "password";
        final String email = "some@email.com";
        final User user = new User(id, username, password, email);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        userService.updateUser(id, username, password, email);

        verify(userRepository).findById(id);
        verify(userRepository).save(user);
        verify(userEventPublisher).publishUserUpdated(new UserUpdatedEvent(user.getId(), user.getUsername(),
                user.getEmail()));
    }
}
