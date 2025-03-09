package com.example.CookMaster.app.user.repository;

import com.example.CookMaster.app.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> getUserByUsername(String username);

    Optional<User> getUserByEmail(String email);

    User getUserById(UUID uuid);

}
