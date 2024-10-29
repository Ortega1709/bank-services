package com.ortega.account.customer;

import com.ortega.account.response.SuccessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "customer-service",
        url = "${application.config.customer-url}"
)
public interface CustomerClient {

    /**
     * Find an existing customer.
     *
     * @param customerId UUID associate to Customer.
     * @return Object SuccessResponse of CustomerDTO.
     */
    @GetMapping("/{customerId}")
    SuccessResponse findCustomerById(@PathVariable("customerId") UUID customerId);

}
