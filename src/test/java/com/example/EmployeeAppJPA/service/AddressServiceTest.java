package com.example.EmployeeAppJPA.service;

import com.example.EmployeeAppJPA.model.Address;
import com.example.EmployeeAppJPA.repository.AddressRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {
    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressService addressService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        System.out.println("Before each annotated method!");
    }

    @BeforeAll
    public static void setUpBeforeClass() {
        System.out.println("@Before all annotated method!");
    }

    @Test
    public void testSaveAddress() {
        Address address = new Address(1,"India");

        // Act
        addressService.saveAddress(address);

        // Assert
        verify(addressRepository, times(1)).save(address);
    }

    @AfterEach
    public void tearDown() {
        System.out.println("@AfterEach annotated method!");
    }

    @AfterAll
    public static void tearDownClass() {
        System.out.println("@AfterAll annotated method!");
    }

}
