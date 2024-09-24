    package com.example.EmployeeAppJPA.service;


    import com.example.EmployeeAppJPA.exception.EmployeeNotFound;
    import com.example.EmployeeAppJPA.model.Address;
    import com.example.EmployeeAppJPA.model.Employee;
    import com.example.EmployeeAppJPA.repository.EmployeeRepository;
    import com.example.EmployeeAppJPA.repository.AddressRepository;
    import com.example.EmployeeAppJPA.serviceinterface.EmployeeInterface;
    import jakarta.transaction.Transactional;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.util.List;

    @Service
    public class EmployeeService implements EmployeeInterface {
        @Autowired
        private EmployeeRepository employeeRepository;

        @Autowired
        private AddressService addressService;

        @Autowired
        private AddressRepository addressRepository;


        @Transactional
        @Override
        public void saveEmployee(Employee employee) {
//            try {
                Employee emp =employeeRepository.save(employee);
                addressService.saveAddress(emp.getAddress());
//                throw new RuntimeException("its not working until it works");
//            }catch(RuntimeException e){
//                throw new RuntimeException("its not working "+e.getMessage() );
//            }
        }

        @Override
        public List<Employee> getAllEmployees() {
            return employeeRepository.findAll();
        }

        @Override
        public Employee getEmployeeById(int employeeId) {
            return employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new EmployeeNotFound(employeeId));
        }

        @Override
        public Employee updateEmployee(int employeeId, Employee employee) {
            if (!employeeRepository.existsById(employeeId)) {
                throw new EmployeeNotFound(employeeId);
            }
            Employee existingEmployee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new EmployeeNotFound(employeeId));

            existingEmployee.setEmployeeName(employee.getEmployeeName());

            if (employee.getAddress() != null) {
                Address existingAddress = existingEmployee.getAddress();
                if (existingAddress != null) {
                    existingAddress.setAddressName(employee.getAddress().getAddressName());
                    addressRepository.save(existingAddress);
                } else {
                    Address newAddress = employee.getAddress();
                    existingEmployee.setAddress(newAddress);
                    addressRepository.save(newAddress);
                }
            }
            return employeeRepository.save(existingEmployee);
        }



        @Override
        public boolean deleteEmployee(int employeeId) {
            if(employeeRepository.existsById(employeeId)){
                employeeRepository.deleteById(employeeId);
                return true;
            } else {
                return false;
            }
        }
    }
