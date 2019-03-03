package com.vanguard.todoplat.userservice.web;

import com.vanguard.todoplat.userservice.api.web.CreateUserRequest;
import com.vanguard.todoplat.userservice.api.web.CreateUserResponse;
import com.vanguard.todoplat.userservice.api.web.UpdateUserRequest;
import com.vanguard.todoplat.userservice.application.UserService;
import com.vanguard.todoplat.userservice.application.domain.exceptions.UserNotFoundException;
import com.vanguard.todoplat.userservice.application.domain.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateUserResponse create(@Valid @RequestBody CreateUserRequest request) {
        User user = userService.createUser(request.getUsername(), request.getPassword(), request.getEmail());
        return new CreateUserResponse(user.getId());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> update(@PathVariable("userId") Long userId,
                                       @Valid @RequestBody UpdateUserRequest request) {
        try {
            userService.updateUser(userId, request.getUsername(), request.getPassword(), request.getEmail());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UserNotFoundException notUsed) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
