package com.ortega.customer.customer;

import com.ortega.customer.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
                .code(HttpStatus.CREATED.value())
                .message("Customer created")
                .data(customerDTO)
                .build();
    }

}
