package com.example.EmployeeAppJPA.service;

import com.example.EmployeeAppJPA.model.Address;
import com.example.EmployeeAppJPA.repository.AddressRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveAddress(Address address){
        addressRepository.save(address);
//        throw new RuntimeException("its not working");
    }
}