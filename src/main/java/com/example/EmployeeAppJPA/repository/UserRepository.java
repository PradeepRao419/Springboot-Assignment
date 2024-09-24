package com.example.EmployeeAppJPA.repository;


import com.example.EmployeeAppJPA.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}

