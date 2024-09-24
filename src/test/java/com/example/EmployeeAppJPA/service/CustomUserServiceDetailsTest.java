package com.example.EmployeeAppJPA.service;

import com.example.EmployeeAppJPA.model.User;
import com.example.EmployeeAppJPA.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomUserServiceDetailsTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        System.out.println("Before each annotated method!");
    }

    @BeforeAll
    public static void setUpBeforeClass() {
        System.out.println("@Before all annotated method!");
    }

    @Test
    public void testLoadUserByUsername_UserFound() {
        // Arrange
        String username = "testUser";
        String password = "testPassword";
        String role = "USER";

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        // Assert
        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals(password, userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_" + role)));
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        // Arrange
        String username = "unknownUser";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername(username);
        });
    }

    @AfterEach
    public void tearDown() {
        System.out.println("@AfterEach annotated method!");
    }

    @AfterAll
    public static void tearDownClass() {
        System.out.println("@AfterAll annotated method!");
    }
}
