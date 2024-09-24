package com.example.EmployeeAppJPA;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class EmployeeControllerIT {

    @Autowired
    private MockMvc mockMvc;

    private static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>(DockerImageName.parse("mysql:latest"));

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    @BeforeAll
    public static void beforeAll() {
        mySQLContainer.start();
    }

    @AfterAll
    public static void afterAll() {
        mySQLContainer.stop();
    }

    private static String jwtToken;

    @BeforeEach
    public void setUp() throws Exception {
        jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwcmFkZWVwIiwicm9sZXMiOlsiUk9MRV9BRE1JTiJdLCJpYXQiOjE3MjU5NDA4MzIsImV4cCI6MTcyNTk3NjgzMn0.Ly6_4QR1d0Ow2C7Ya08r774eDoSUxomm5cXxh9MgF7o";
        System.out.println("Generated JWT Token: " + jwtToken);
    }

    @Test
    @Order(1)
    @WithMockUser(username = "pradeep",roles = {"ADMIN"})
    public void testUserController_CreateUser() throws Exception {
        String userJson = "{\"username\":\"arun\",\"password\":\"arun\",\"role\":\"USER\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/createUser")
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));
    }

    @Test
    @Order(2)
    @WithMockUser(username = "pradeep", roles = {"ADMIN"})
    public void testEmployeeController_AddEmployee() throws Exception {
        String employeeJson = "{\"employeeName\":\"John Doe\",\"address\":{\"addressName\":\"New York\"}}";

        mockMvc.perform(MockMvcRequestBuilders.post("/employees/add")
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(employeeJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string("Employee saved successfully!"));
    }

    @Test
    @Order(3)
    @WithMockUser(username = "pradeep", roles = {"ADMIN"})
    public void testEmployeeController_GetEmployeeById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/employees/get/1")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    @WithMockUser(username = "pradeep", roles = {"ADMIN"})
    public void testEmployeeController_GetAllEmployees() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/employees/getAll")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andReturn();
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        System.out.println("JSON Response: " + jsonResponse);
    }

    @Test
    @Order(5)
    @WithMockUser(username = "pradeep", roles = {"ADMIN"})
    public void testEmployeeController_UpdateEmployee() throws Exception {
        String updatedEmployeeJson = "{\"employeeName\":\"Jane Doe\",\"address\":{\"addressName\":\"Boston\"}}";

        mockMvc.perform(MockMvcRequestBuilders.put("/employees/update/1")
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(updatedEmployeeJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    @WithMockUser(username = "pradeep", roles = {"ADMIN"})
    public void testEmployeeController_DeleteEmployee() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/employees/delete/1")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee deleted!"));
    }
}
