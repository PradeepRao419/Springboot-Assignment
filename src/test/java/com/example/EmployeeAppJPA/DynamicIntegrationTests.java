package com.example.EmployeeAppJPA;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DynamicIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    private String jwtToken;

    private void setUp(String username, String password) throws Exception {
        String loginRequestJson = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";

        jwtToken = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .content(loginRequestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @ParameterizedTest
    @CsvSource({
            "pradeep, pradeep",
            "deep, deep"
    })
    public void testEmployeeController_AddEmployee(String username, String password) throws Exception {
        setUp(username, password);

        String employeeJson = "{\"employeeName\":\"John Doe\",\"address\":{\"addressName\":\"New York\"}}";

        mockMvc.perform(MockMvcRequestBuilders.post("/employees/add")
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(employeeJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string("Employee saved successfully!"));
    }

    @ParameterizedTest
    @CsvSource({
            "pradeep, pradeep",
            "deep, deep"
    })
    public void testEmployeeController_GetAllEmployees(String username, String password) throws Exception {
        setUp(username, password);

        mockMvc.perform(MockMvcRequestBuilders.get("/employees/getAll")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @CsvSource({
            "pradeep, pradeep, 29",
            "deep, deep, 29"
    })
    public void testEmployeeController_GetEmployeeById(String username, String password, int id) throws Exception {
        setUp(username, password);

        mockMvc.perform(MockMvcRequestBuilders.get("/employees/get/" + id)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @CsvSource({
            "pradeep, pradeep, 29",
            "deep, deep, 29"
    })
    public void testEmployeeController_UpdateEmployee(String username, String password,int id) throws Exception {
        setUp(username, password);

        String updatedEmployeeJson = "{\"employeeName\":\"Jane Doe\",\"address\":{\"addressName\":\"Boston\"}}";

        mockMvc.perform(MockMvcRequestBuilders.put("/employees/update/"+id)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(updatedEmployeeJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @CsvSource({
            "pradeep, pradeep, 29",
            "deep, deep, 29"
    })
    public void testEmployeeController_DeleteEmployee(String username, String password,int id) throws Exception {
        setUp(username, password);

        mockMvc.perform(MockMvcRequestBuilders.delete("/employees/delete/"+ id)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee deleted!"));
    }

    @ParameterizedTest
    @CsvSource({
            "pradeep, pradeep, ADMIN",
            "deep, deep, USER"
    })
    public void testUserController_CreateUser(String username, String password, String role) throws Exception {
        setUp(username, password);

        String userJson = "{\"username\":\"new_user\",\"password\":\"password\",\"role\":\"" + role + "\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/createUser")
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));
    }
}
