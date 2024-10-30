package com.ortega.customer.customer;

import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
public class CustomerMapper {

    /**
     * Map Customer request to Customer.
     *
     * @param request Object that contains information about customer request.
     * @return Object Customer.
     */
    public Customer toCustomer(CustomerRequest request) {
        if (request == null) return null;

        return Customer.builder()
                .customerId(request.customerId())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .phone(request.phone())
                .address(request.address())
                .gender(request.gender())
                .dateOfBirth(request.dateOfBirth())
                .build();
    }

    /**
     * Map Customer to a Customer data transfer object.
     *
     * @param customer Object that contains information of customer.
     * @return Object Customer data transfer object.
     */
    public CustomerDTO toDTO(Customer customer) {
        if (customer == null) return null;

        return CustomerDTO.builder()
                .customerId(customer.getCustomerId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .address(customer.getAddress())
                .gender(customer.getGender())
                .dateOfBirth(customer.getDateOfBirth())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .build();
    }

}
