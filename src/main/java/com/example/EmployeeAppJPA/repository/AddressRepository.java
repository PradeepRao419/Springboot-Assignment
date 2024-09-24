package com.example.EmployeeAppJPA.repository;

import com.example.EmployeeAppJPA.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address,Integer> {
}
