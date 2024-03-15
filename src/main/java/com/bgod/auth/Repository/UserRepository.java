package com.bgod.auth.Repository;

import com.bgod.auth.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User getUserById(UUID id);
    User getUserByEmail(String email);
}
