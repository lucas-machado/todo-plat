package com.vanguard.todoplat.userservice.application.domain.repositories;

import com.vanguard.todoplat.userservice.application.domain.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
