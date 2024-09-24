package com.example.EmployeeAppJPA.controller;

import com.example.EmployeeAppJPA.model.User;
import com.example.EmployeeAppJPA.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.example.EmployeeAppJPA.service.UserService;

@RestController
@RequestMapping("/admin")
public class UserController {
    private final UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/createUser")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        System.out.println("Inside user controller createUser method.");
        userService.createUser(
                user.getUsername(),
                user.getPassword(),
                user.getRole()
        );
        return ResponseEntity.ok("User registered successfully");
    }
}

