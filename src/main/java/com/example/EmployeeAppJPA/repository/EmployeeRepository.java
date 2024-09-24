package com.example.EmployeeAppJPA.repository;


import com.example.EmployeeAppJPA.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee,Integer> {
}
