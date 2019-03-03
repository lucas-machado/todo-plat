package com.vanguard.todoplat.userservice.web;

import com.vanguard.todoplat.userservice.api.web.CreateUserRequest;
import com.vanguard.todoplat.userservice.api.web.UpdateUserRequest;
import com.vanguard.todoplat.userservice.application.UserService;
import com.vanguard.todoplat.userservice.application.domain.model.User;
import org.junit.Test;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private final UserService userService = mock(UserService.class);
    private final UserController userController = new UserController(userService);
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "some@email.com";
    private static final Long ID = 1L;

    @Test
    public void shouldCreateUser() {
        when(userService.createUser(USERNAME, PASSWORD, EMAIL)).thenReturn(new User(ID, USERNAME, PASSWORD, EMAIL));
        CreateUserRequest createUserRequest = new CreateUserRequest(USERNAME, PASSWORD, EMAIL);

        given().
                       standaloneSetup(userController).
                       contentType("application/json").
                       body(createUserRequest).
        when().
                       post("/users").
        then().
                       statusCode(201).
                       body("userId", equalTo(ID.intValue()));
    }

    @Test
    public void shouldUpdateUser() {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest(USERNAME, PASSWORD, EMAIL);

        given().
                       standaloneSetup(userController).
                       contentType("application/json").
                       body(updateUserRequest).
        when().
                       put("/users/{userId}", ID).
        then().
                       statusCode(200);
    }
}
