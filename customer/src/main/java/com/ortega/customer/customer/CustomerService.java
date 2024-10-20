package com.ortega.customer.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerDTO createCustomer(CustomerRequest request) {
        Customer customer = customerMapper.toCustomer(request);
        return customerMapper.toDTO(customerRepository.save(customer));
    }

}
