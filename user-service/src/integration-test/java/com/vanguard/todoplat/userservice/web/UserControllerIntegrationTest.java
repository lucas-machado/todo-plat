package com.vanguard.todoplat.userservice.web;

import com.vanguard.todoplat.userservice.api.web.CreateUserRequest;
import com.vanguard.todoplat.userservice.application.domain.model.User;
import com.vanguard.todoplat.userservice.application.domain.repositories.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerIntegrationTest {

    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldCreateUser() {
        CreateUserRequest createUserRequest = new CreateUserRequest("username",
                "password", "email@email.com");

        final Integer newUserId =
        given().
                       standaloneSetup(userController).
                       contentType("application/json").
                       body(createUserRequest).
        when().
                       post("/users").
        then().
                       statusCode(201).
        extract().
                       path("userId");

        Optional<User> possibleUser = userRepository.findById(newUserId.longValue());

        assertTrue(possibleUser.isPresent());

        User user = possibleUser.get();

        assertEquals(createUserRequest.getUsername(), user.getUsername());
        assertEquals(createUserRequest.getPassword(), user.getPassword());
        assertEquals(createUserRequest.getEmail(), user.getEmail());
    }
}
