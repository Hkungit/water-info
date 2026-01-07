package com.seecen.waterinfo.repository;

import com.seecen.waterinfo.domain.entity.User;
import com.seecen.waterinfo.domain.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    long countByRole(UserRole role);
}
