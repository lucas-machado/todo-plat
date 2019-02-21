package com.vanguard.todoplat.userservice.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vanguard.todoplat.userservice.api.events.UserCreatedEvent;
import com.vanguard.todoplat.userservice.api.events.UserUpdatedEvent;
import com.vanguard.todoplat.userservice.domain.exceptions.UserNotFoundException;
import com.vanguard.todoplat.userservice.domain.model.User;
import com.vanguard.todoplat.userservice.domain.repositories.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageChannel output;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MessageCollector messageCollector;

    private final String USERNAME = "username";
    private final String PASSWORD = "password";
    private final String EMAIL = "some@email.com";
    private final String NEWEMAIL = "newsome@email.com";

    @Test
    public void shouldCreateUser() throws JsonProcessingException {
        User user = userService.createUser(USERNAME, PASSWORD, EMAIL);
        Optional<User> loadedUser = userRepository.findById(user.getId());
        UserCreatedEvent userCreatedEvent = new UserCreatedEvent(user.getId(), user.getUsername(), user.getEmail());
        Message<?> userCreatedMessage = messageCollector.forChannel(output).poll();

        assertTrue(loadedUser.isPresent());
        assertEquals(USERNAME, loadedUser.get().getUsername());
        assertEquals(PASSWORD, loadedUser.get().getPassword());
        assertEquals(EMAIL, loadedUser.get().getEmail());
        assertNotNull(userCreatedMessage);
        assertEquals(new ObjectMapper().writeValueAsString(userCreatedEvent), userCreatedMessage.getPayload());
    }

    @Test
    public void shouldUpdateUser() throws UserNotFoundException, JsonProcessingException {
        User user = userService.createUser(USERNAME, PASSWORD, EMAIL);
        messageCollector.forChannel(output).poll(); //extracting the create message

        userService.updateUser(user.getId(), user.getUsername(), user.getPassword(), NEWEMAIL);
        Optional<User> loadedUser = userRepository.findById(user.getId());
        UserUpdatedEvent userUpdatedEvent = new UserUpdatedEvent(user.getId(), user.getUsername(), NEWEMAIL);
        Message<?> userUpdatedMessage = messageCollector.forChannel(output).poll();

        assertTrue(loadedUser.isPresent());
        assertEquals(NEWEMAIL, loadedUser.get().getEmail());
        assertNotNull(userUpdatedMessage);
        assertEquals(new ObjectMapper().writeValueAsString(userUpdatedEvent), userUpdatedMessage.getPayload());
    }
}
