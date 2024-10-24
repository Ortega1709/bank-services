package com.ortega.customer.customer;

import com.ortega.customer.event.customer.CustomerCreatedEvent;
import com.ortega.customer.event.customer.CustomerDeletedEvent;
import com.ortega.customer.exception.CustomerAlreadyExistsException;
import com.ortega.customer.exception.CustomerNotFoundException;
import jakarta.transaction.Transactional;
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

    @Transactional
    public CustomerDTO createCustomer(CustomerRequest request) {
        if (customerRepository.existsByEmail(request.email())) {
            throw new CustomerAlreadyExistsException(
                    String.format("Customer with email '%s' already exists", request.email())
            );
        }

        Customer customer = customerMapper.toCustomer(request);
        customerRepository.saveAndFlush(customer);

        customerKafkaProducer.produceCustomerCreatedEvent(
                new CustomerCreatedEvent(
                        customer.getCustomerId(),
                        customer.getFirstName(),
                        customer.getLastName()
                )
        );

        return customerMapper.toDTO(customer);
    }

    public CustomerDTO updateCustomer(CustomerRequest request) {
        customerRepository.findById(request.customerId()).orElseThrow(
                () -> new CustomerNotFoundException(
                        String.format("Customer with id '%s' not found", request.customerId()
                        )
                )
        );

        Customer customer = customerMapper.toCustomer(request);
        return customerMapper.toDTO(customerRepository.save(customer));
    }

    public Page<CustomerDTO> findAll(Pageable pageable) {
        return customerRepository
                .findAll(pageable)
                .map(customerMapper::toDTO);
    }

    public CustomerDTO findById(UUID customerId) {
        return customerRepository
                .findById(customerId)
                .map(customerMapper::toDTO).orElseThrow(
                        () -> new CustomerNotFoundException(
                                String.format("Customer with id '%s' not found", customerId)
                        )
                );
    }

    public void deleteCustomerById(UUID customerId) {
        customerRepository.deleteById(customerId);

        customerKafkaProducer.produceCustomerDeletedEvent(
                new CustomerDeletedEvent(customerId)
        );
    }

}
