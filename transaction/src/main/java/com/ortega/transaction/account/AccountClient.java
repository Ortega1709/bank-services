package com.ortega.transaction.account;

import com.ortega.transaction.response.SuccessResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "account-service",
        url = "${application.config.account-url}"
)
public interface AccountClient {

    @GetMapping("/{accountNumber}")
    SuccessResponse findByAccountNumber(@PathVariable("accountNumber") String accountNumber);

    @PostMapping("/debit")
    SuccessResponse debitAccount(@Valid @RequestBody AccountTransactionRequest request);

    @PostMapping("/credit")
    SuccessResponse creditAccount(@Valid @RequestBody AccountTransactionRequest request);
}
