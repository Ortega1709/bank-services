package com.ortega.customer.customer;

import com.ortega.customer.event.customer.CustomerCreatedEvent;
import com.ortega.customer.event.customer.CustomerDeletedEvent;
import com.ortega.customer.exception.CustomerAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerKafkaProducer customerKafkaProducer;
    private final CustomerMapper customerMapper;


    public void createCustomer(CustomerRequest request) {
        if (customerRepository.existsByEmail(request.email())) {
            throw new CustomerAlreadyExistsException(
                    String.format("Customer with email '%s' already exists", request.email())
            );
        }

        Customer customer = customerMapper.toCustomer(request);
        customerRepository.saveAndFlush(customer);

        CustomerCreatedEvent event = CustomerCreatedEvent.builder()
                .customerId(customer.getCustomerId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .build();

        customerKafkaProducer.produceCustomerCreatedEvent(event);
    }

    public Page<CustomerDTO> findAll(Pageable pageable) {
        return customerRepository
                .findAll(pageable)
                .map(customerMapper::toDTO);
    }

    public void deleteCustomerById(UUID customerId) {
        customerRepository.deleteById(customerId);

        CustomerDeletedEvent event = CustomerDeletedEvent
                .builder()
                .customerId(customerId)
                .build();

        customerKafkaProducer.produceCustomerDeletedEvent(event);
    }


}
