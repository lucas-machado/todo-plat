package com.vanguard.todoplat.userservice.domain;

import com.vanguard.todoplat.userservice.api.events.UserCreatedEvent;
import com.vanguard.todoplat.userservice.api.events.UserUpdatedEvent;
import com.vanguard.todoplat.userservice.domain.exceptions.UserNotFoundException;
import com.vanguard.todoplat.userservice.domain.model.User;
import com.vanguard.todoplat.userservice.domain.publishers.UserEventPublisher;
import com.vanguard.todoplat.userservice.domain.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;
    private UserEventPublisher userEventPublisher;

    public UserService(UserRepository userRepository, UserEventPublisher userEventPublisher) {
        this.userRepository = userRepository;
        this.userEventPublisher = userEventPublisher;
    }

    public User createUser(String username, String password, String email) {
        User user = new User(username, password, email);
        User saved = userRepository.save(user);
        userEventPublisher.publishUserCreated(new UserCreatedEvent(saved.getId(), saved.getUsername(),
                saved.getEmail()));
        return saved;
    }

    public User updateUser(Long userId, String username, String password, String email) throws UserNotFoundException {
        return userRepository.findById(userId).map(user -> {
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            User updated = userRepository.save(user);
            userEventPublisher.publishUserUpdated(new UserUpdatedEvent(updated.getId(), updated.getUsername(),
                    updated.getEmail()));
            return updated;
        }).orElseThrow(() -> new UserNotFoundException(userId));
    }
}
