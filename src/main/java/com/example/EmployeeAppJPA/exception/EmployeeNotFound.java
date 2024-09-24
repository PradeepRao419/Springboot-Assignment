package com.example.EmployeeAppJPA.exception;

public class EmployeeNotFound extends RuntimeException {
    public EmployeeNotFound(int id) {
        super("Employee with id "+id+" not found");
    }
}
