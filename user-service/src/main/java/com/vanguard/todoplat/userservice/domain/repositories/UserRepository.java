package com.vanguard.todoplat.userservice.domain.repositories;

import com.vanguard.todoplat.userservice.domain.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
