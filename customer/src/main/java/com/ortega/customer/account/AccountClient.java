package com.ortega.customer.account;

import com.ortega.customer.response.SuccessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(
        name = "account-service",
        url = "${application.config.account-url}"
)
public interface AccountClient {

    @PostMapping
    SuccessResponse createAccount(@RequestBody AccountRequest accountRequest);

    @DeleteMapping("/{customerId}")
    SuccessResponse deleteAccountByCustomerId(@PathVariable("customerId") UUID customerId);

}
