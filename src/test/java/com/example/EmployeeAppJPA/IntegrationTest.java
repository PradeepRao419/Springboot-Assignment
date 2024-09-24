package com.example.EmployeeAppJPA;

import com.example.EmployeeAppJPA.model.Address;
import com.example.EmployeeAppJPA.model.Employee;
import com.example.EmployeeAppJPA.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @BeforeEach
    public void setUp() {
        // Mock the service methods that have return values
        when(employeeService.getEmployeeById(anyInt())).thenReturn(new Employee(28, "John Doe", new Address(1,"New York")));
        when(employeeService.deleteEmployee(anyInt())).thenReturn(true);
    }

    @Test
    public void testEmployeeController_AddEmployee() throws Exception {
        String employeeJson = "{\"employeeName\":\"John Doe\",\"address\":{\"addressName\":\"New York\"}}";

        mockMvc.perform(post("/employees/add")
                        .content(employeeJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string("Employee saved successfully!"));

        // Verify that saveEmployee was called with any Employee object
        verify(employeeService, times(1)).saveEmployee(any(Employee.class));
    }

    @Test
    public void testEmployeeController_GetAllEmployees() throws Exception {
        mockMvc.perform(get("/employees/getAll"))
                .andExpect(status().isOk());
    }

    @Test
    public void testEmployeeController_GetEmployeeById() throws Exception {
        mockMvc.perform(get("/employees/get/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testEmployeeController_UpdateEmployee() throws Exception {
        String updatedEmployeeJson = "{\"employeeName\":\"Jane Doe\",\"address\":{\"addressName\":\"Boston\"}}";

        mockMvc.perform(put("/employees/update/1")
                        .content(updatedEmployeeJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testEmployeeController_DeleteEmployee() throws Exception {
        mockMvc.perform(delete("/employees/delete/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee deleted!"));
    }
}
