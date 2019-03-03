package com.vanguard.taskplat.notificationservice.application.domain.repositories;

import com.vanguard.taskplat.notificationservice.application.domain.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
