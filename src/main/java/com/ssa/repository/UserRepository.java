package com.ssa.repository;

import com.ssa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserEmailAndUserPassword(String email, String password);

    boolean existsByUserEmail(String userEmail);

    Optional<User> findByUserEmail(String email);

    boolean existsByUserEmailAndIdNot(String email, Long userId);

    boolean existsByUserNameAndIdNot(String userName, Long userId);

    boolean existsByUserName(String userName);
}