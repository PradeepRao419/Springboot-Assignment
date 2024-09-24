package com.example.EmployeeAppJPA.controller;

import com.example.EmployeeAppJPA.exception.EmployeeNotFound;
import com.example.EmployeeAppJPA.exception.InvalidInput;
import com.example.EmployeeAppJPA.model.Address;
import com.example.EmployeeAppJPA.model.Employee;
import com.example.EmployeeAppJPA.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @PostMapping("/add")
    public ResponseEntity<String> saveEmployee(@RequestBody Employee employee) {
        System.out.println("Inside employee controller saveEmployee method.");
        if(employee == null || employee.getAddress() == null) {
            throw new InvalidInput("Invalid input!");
        }
        employeeService.saveEmployee(employee);
        return new ResponseEntity<String>("Employee saved successfully!", HttpStatus.CREATED);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Employee>> getAllEmployee(){
        System.out.println("Inside employee controller getAllEmployee method.");
        return new ResponseEntity<List<Employee>>(employeeService.getAllEmployees(),HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Employee> getByID(@PathVariable("id") int empId){
        System.out.println("Inside employee controller getByID method.");
        return new ResponseEntity<Employee>(employeeService.getEmployeeById(empId), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee, @PathVariable int id) {
        System.out.println("Inside employee controller updateEmployee method.");
        Employee updatedEmployee = employeeService.updateEmployee(id, employee);
        return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") int empId){
        System.out.println("Inside employee controller deleteEmployee method.");
        System.out.println(empId);
        if(employeeService.deleteEmployee(empId)) {
            return new ResponseEntity<String>("Employee deleted!", HttpStatus.OK);
        }
        throw new EmployeeNotFound(empId);
    }
}
