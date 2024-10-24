package com.ortega.account.account;

import com.ortega.account.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponse findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(page, size);
        Page<AccountDTO> accountDTOS = accountService.findAll(pageable);

        return SuccessResponse.builder()
                .status("Account fetched successfully")
                .code(HttpStatus.OK.value())
                .data(accountDTOS)
                .build();
    }

    @GetMapping("/{accountNumber}")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponse findByAccountNumber(@PathVariable("accountNumber") String accountNumber) {
        AccountDTO accountDTO = accountService.findByAccountNumber(accountNumber);
        return SuccessResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Account fetched successfully")
                .data(accountDTO)
                .build();
    }

}
