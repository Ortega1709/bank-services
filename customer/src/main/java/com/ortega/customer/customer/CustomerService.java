package com.ortega.customer.customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ortega.customer.account.AccountClient;
import com.ortega.customer.account.AccountDTO;
import com.ortega.customer.account.AccountRequest;
import com.ortega.customer.event.customer.CustomerCreatedEvent;
import com.ortega.customer.event.customer.CustomerDeletedEvent;
import com.ortega.customer.exception.BusinessException;
import com.ortega.customer.exception.CustomerAlreadyExistsException;
import com.ortega.customer.exception.CustomerNotFoundException;
import com.ortega.customer.response.SuccessResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerKafkaProducer customerKafkaProducer;
    private final CustomerMapper customerMapper;
    private final AccountClient accountClient;

    @Transactional
    public CustomerDTO createCustomer(CustomerRequest request) throws ParseException {
        if (customerRepository.existsByEmail(request.email())) {
            throw new CustomerAlreadyExistsException(
                    String.format("Customer with email '%s' already exists", request.email())
            );
        }

        Customer customer = customerMapper.toCustomer(request);
        customerRepository.saveAndFlush(customer);

        SuccessResponse successResponse = accountClient.createAccount(
                new AccountRequest(customer.getCustomerId())
        );

        if (successResponse.getCode() != HttpStatus.CREATED.value())
            throw new BusinessException("Something went wrong");


        AccountDTO accountDTO = new ObjectMapper().convertValue(successResponse.getData(), AccountDTO.class);
        customerKafkaProducer.produceCustomerCreatedEvent(
                new CustomerCreatedEvent(
                        accountDTO.getAccountNumber(),
                        customer.getEmail(),
                        customer.getFirstName(),
                        customer.getLastName(),
                        accountDTO.getPin()
                )
        );

        return customerMapper.toDTO(customer);
    }

    public CustomerDTO updateCustomer(CustomerRequest request) throws ParseException {
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

    @Transactional
    public void deleteCustomerById(UUID customerId) {
        customerRepository.deleteById(customerId);
        accountClient.deleteAccountByCustomerId(customerId);
    }

}
