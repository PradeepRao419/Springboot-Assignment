package com.example.EmployeeAppJPA.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.EmployeeAppJPA.exception.EmployeeNotFound;
import com.example.EmployeeAppJPA.model.Address;
import com.example.EmployeeAppJPA.model.Employee;
import com.example.EmployeeAppJPA.repository.EmployeeRepository;
import com.example.EmployeeAppJPA.repository.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;
import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private AddressService addressService;

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveEmployee_Success() {
        // Arrange
        Employee employee = new Employee();
        Address address = new Address();
        employee.setAddress(address);

        when(employeeRepository.save(employee)).thenReturn(employee);

        // Act
        employeeService.saveEmployee(employee);

        // Assert
        verify(employeeRepository, times(1)).save(employee);
        verify(addressService, times(1)).saveAddress(address);
    }

    @Test
    public void testGetAllEmployees_Success() {
        // Arrange
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(employeeRepository.findAll()).thenReturn(employees);

        // Act
        List<Employee> result = employeeService.getAllEmployees();

        // Assert
        assertEquals(2, result.size());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    public void testGetEmployeeById_Success() {
        // Arrange
        int employeeId = 1;
        Employee employee = new Employee();
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        // Act
        Employee result = employeeService.getEmployeeById(employeeId);

        // Assert
        assertEquals(employee, result);
        verify(employeeRepository, times(1)).findById(employeeId);
    }

    @Test
    public void testGetEmployeeById_NotFound() {
        // Arrange
        int employeeId = 1;
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EmployeeNotFound.class, () -> employeeService.getEmployeeById(employeeId));
        verify(employeeRepository, times(1)).findById(employeeId);
    }

    @Test
    public void testUpdateEmployee_Success() {
        // Arrange
        int employeeId = 1;
        Employee existingEmployee = new Employee();
        Employee updatedEmployee = new Employee();
        updatedEmployee.setEmployeeName("Updated Name");
        Address address = new Address();
        updatedEmployee.setAddress(address);

        when(employeeRepository.existsById(employeeId)).thenReturn(true);
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(existingEmployee)).thenReturn(existingEmployee);

        // Act
        Employee result = employeeService.updateEmployee(employeeId, updatedEmployee);

        // Assert
        assertEquals(existingEmployee, result);
        verify(employeeRepository, times(1)).existsById(employeeId);
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeRepository, times(1)).save(existingEmployee);
        verify(addressRepository, times(1)).save(address);
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2,3})
    public void testUpdateEmployee_NotFound(int number) {
        // Arrange
        int employeeId = number;
        Employee updatedEmployee = new Employee();

        when(employeeRepository.existsById(employeeId)).thenReturn(false);

        // Act & Assert
        assertThrows(EmployeeNotFound.class, () -> employeeService.updateEmployee(employeeId, updatedEmployee));
        verify(employeeRepository, times(1)).existsById(employeeId);
        verify(employeeRepository, never()).findById(employeeId);
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    @Disabled
    public void testDeleteEmployee_Success() {
        // Arrange
        int employeeId = 1;
        when(employeeRepository.existsById(employeeId)).thenReturn(true);

        // Act
        boolean result = employeeService.deleteEmployee(employeeId);

        // Assert
        assertTrue(result);
        verify(employeeRepository, times(1)).existsById(employeeId);
        verify(employeeRepository, times(1)).deleteById(employeeId);
    }

    @Test
    @DisplayName("Delete Employee Not Found")
    public void testDeleteEmployee_NotFound() {
        // Arrange
        int employeeId = 1;
        when(employeeRepository.existsById(employeeId)).thenReturn(false);

        // Act
        boolean result = employeeService.deleteEmployee(employeeId);

        // Assert
        assertFalse(result);
        verify(employeeRepository, times(1)).existsById(employeeId);
        verify(employeeRepository, never()).deleteById(employeeId);
    }
}
