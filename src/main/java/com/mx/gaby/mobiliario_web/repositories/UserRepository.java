package com.mx.gaby.mobiliario_web.repositories;

import com.mx.gaby.mobiliario_web.model.entitites.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsernameAndFgActive(String userName, String fgActive);
}
