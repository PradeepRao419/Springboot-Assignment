package com.example.EmployeeAppJPA.exception;

public class InvalidInput extends RuntimeException {
    public InvalidInput(String message) {
        super(message);
    }
}