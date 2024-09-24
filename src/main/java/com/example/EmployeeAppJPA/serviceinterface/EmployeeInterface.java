package com.example.EmployeeAppJPA.serviceinterface;

import com.example.EmployeeAppJPA.model.Address;
import com.example.EmployeeAppJPA.model.Employee;

import java.util.List;

public interface EmployeeInterface {
    void saveEmployee(Employee employee);
    List<Employee> getAllEmployees();
    Employee getEmployeeById(int employeeId);
    Employee updateEmployee(int employeeId, Employee employee);
    boolean deleteEmployee(int employeeId);
}
