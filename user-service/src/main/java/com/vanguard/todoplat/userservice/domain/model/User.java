package com.vanguard.todoplat.userservice.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Size(min = 8)
    private String username;

    @NotNull
    @Size(min = 8)
    private String password;

    @NotNull
    @Email
    private String email;

    public User(@NotNull @Size(min = 8) String username,
                @NotNull @Size(min = 8) String password,
                @NotNull @Email String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
