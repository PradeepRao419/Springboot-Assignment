package com.example.EmployeeAppJPA.service;

import com.example.EmployeeAppJPA.model.User;
import com.example.EmployeeAppJPA.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() {
        // Arrange
        String username = "testuser";
        String password = "password123";
        String role = "USER";
        String encodedPassword = "encodedPassword123";

        User mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setPassword(encodedPassword);
        mockUser.setRole(role);

        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // Act
        userService.createUser(username, password, role);

        // Assert
        verify(passwordEncoder).encode(password);
        verify(userRepository).save(any(User.class));
    }
}
