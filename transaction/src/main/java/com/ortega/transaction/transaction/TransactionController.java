package com.ortega.transaction.transaction;

import com.ortega.transaction.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * Handle transfer money request.
     *
     * @param request Object that contains information about transaction.
     * @return Object SuccessResponse of TransactionDTO.
     */
    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponse transfer(@RequestBody TransactionRequest request) {
        TransactionDTO transactionDTO = transactionService.transferMoney(request);

        return SuccessResponse.builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Transfer success")
                .data(transactionDTO)
                .build();
    }

    /**
     * Handle withdraw money request.
     *
     * @param request Object that contains information about transaction.
     * @return Object SuccessResponse of TransactionDTO.
     */
    @PostMapping("/withdraw")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponse withdraw(@RequestBody TransactionRequest request) {
        TransactionDTO transactionDTO = transactionService.withdrawMoney(request);

        return SuccessResponse.builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Withdraw success")
                .data(transactionDTO)
                .build();
    }

    /**
     * Handle deposit money request.
     *
     * @param request Object that contains information about transaction.
     * @return Object SuccessResponse of TransactionDTO.
     */
    @PostMapping("/deposit")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponse deposit(@RequestBody TransactionRequest request) {
        TransactionDTO transactionDTO = transactionService.depositMoney(request);

        return SuccessResponse.builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Deposit success")
                .data(transactionDTO)
                .build();
    }

    /**
     * Find all transactions request.
     *
     * @param page batch of transactions fetched.
     * @param size max size of transactions per page.
     * @return
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponse findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TransactionDTO> transactionDTOPage = transactionService.findAll(pageable);

        return SuccessResponse.builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Transaction fetched successfully")
                .data(transactionDTOPage)
                .build();
    }
}
