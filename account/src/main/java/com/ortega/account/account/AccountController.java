package com.ortega.account.account;

import com.ortega.account.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse createAccount(@RequestBody AccountRequest request) {
        AccountDTO accountDTO = accountService.createAccount(request);

        return SuccessResponse.builder()
                .status("success")
                .message("Account created successfully")
                .code(HttpStatus.CREATED.value())
                .data(accountDTO)
                .build();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponse findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(page, size);
        Page<AccountDTO> accountDTOS = accountService.findAll(pageable);

        return SuccessResponse.builder()
                .status("success")
                .message("Accounts fetched successfully")
                .code(HttpStatus.OK.value())
                .data(accountDTOS)
                .build();
    }

    @GetMapping("/{accountNumber}")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponse findByAccountNumber(@PathVariable("accountNumber") String accountNumber) {
        AccountDTO accountDTO = accountService.findByAccountNumber(accountNumber);
        return SuccessResponse.builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Account fetched successfully")
                .data(accountDTO)
                .build();
    }

    @DeleteMapping("/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponse deleteAccount(@PathVariable("customerId") UUID customerId) {
        accountService.deleteAccountByCustomerId(customerId);

        return SuccessResponse.builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Account deleted successfully")
                .data(null)
                .build();
    }

}
