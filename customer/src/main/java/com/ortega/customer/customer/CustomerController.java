package com.ortega.customer.customer;

import com.ortega.customer.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse createCustomer(@RequestBody CustomerRequest request) {
        CustomerDTO customerDTO = customerService.createCustomer(request);
        return SuccessResponse.builder()
                .status("success")
                .message("Customer created successfully")
                .code(HttpStatus.CREATED.value())
                .data(customerDTO)
                .build();
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponse updateCustomer(@RequestBody CustomerRequest request) {
        CustomerDTO customerDTO = customerService.updateCustomer(request);
        return SuccessResponse.builder()
                .status("success")
                .message("Customer updated successfully")
                .code(HttpStatus.OK.value())
                .data(customerDTO)
                .build();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponse findAll(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(page, size);
        Page<CustomerDTO> customers = customerService.findAll(pageable);

        return SuccessResponse.builder()
                .status("success")
                .message("Customers fetched successfully")
                .code(HttpStatus.OK.value())
                .data(customers)
                .build();
    }

    @GetMapping("/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponse findById(@PathVariable("customerId") UUID customerId) {
        CustomerDTO customerDTO = customerService.findById(customerId);
        return SuccessResponse.builder()
                .status("success")
                .message("Customers fetched successfully")
                .code(HttpStatus.OK.value())
                .data(customerDTO)
                .build();
    }

    @DeleteMapping("/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponse deleteCustomer(@PathVariable("customerId") UUID customerId) {
        customerService.deleteCustomerById(customerId);
        return SuccessResponse.builder()
                .status("success")
                .message("Customer deleted successfully")
                .code(HttpStatus.OK.value())
                .data(null)
                .build();
    }

}
