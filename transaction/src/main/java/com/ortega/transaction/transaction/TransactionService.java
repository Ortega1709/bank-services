package com.ortega.transaction.transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ortega.transaction.account.AccountClient;
import com.ortega.transaction.account.AccountDTO;
import com.ortega.transaction.account.AccountTransactionRequest;
import com.ortega.transaction.exception.BusinessException;
import com.ortega.transaction.exception.InsufficientBalanceException;
import com.ortega.transaction.fees.FeesService;
import com.ortega.transaction.response.SuccessResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final FeesService feesService;
    private final ObjectMapper objectMapper;
    private final AccountClient accountClient;
    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;


    @Transactional(rollbackOn = BusinessException.class)
    public TransactionDTO transferMoney(TransactionRequest request) {
        BigDecimal feesAmount = feesService.calculateFees(
                TransactionType.TRANSFER,
                request.amount()
        );

        return TransactionDTO.builder().build();
    }

    @Transactional(dontRollbackOn = {BusinessException.class, InsufficientBalanceException.class})
    public TransactionDTO withdrawMoney(TransactionRequest request) {
        BigDecimal feesAmount = feesService.calculateFees(
                TransactionType.WITHDRAW,
                request.amount()
        );

        Transaction transaction = Transaction.builder()
                .accountNumber(request.accountNumber())
                .toAccountNumber(null)
                .feesAmount(feesAmount.subtract(request.amount()))
                .reference(TransactionUtil.generateTransactionRef(10))
                .amount(feesAmount)
                .transactionType(TransactionType.WITHDRAW)
                .transactionStatus(TransactionStatus.SUCCESS)
                .build();

        transactionRepository.saveAndFlush(transaction);

        try {
            SuccessResponse findAccountResponse = accountClient.findByAccountNumber(request.accountNumber());
            if (findAccountResponse.getCode() != HttpStatus.OK.value())
                throw new BusinessException("Account not found");

            AccountDTO accountDTO = objectMapper.convertValue(findAccountResponse.getData(), AccountDTO.class);
            if (accountDTO.getBalance().compareTo(request.amount()) < 0)
                throw new InsufficientBalanceException(
                        String.format(
                                "Insufficient balance for debit transaction %.2f",
                                request.amount().setScale(2, RoundingMode.HALF_UP)
                        )
                );

            SuccessResponse successResponse = accountClient.debitAccount(
                    new AccountTransactionRequest(
                            accountDTO.getAccountId(),
                            feesAmount
                    )
            );

            if (successResponse.getCode() != HttpStatus.OK.value())
                throw new BusinessException("Account not debited");

            // TODO: Produce TransactionDebitSuccessEvent()
            return transactionMapper.toDTO(transaction);

        } catch (BusinessException e) {

            transaction.setTransactionStatus(TransactionStatus.FAILED);
            transactionRepository.save(transaction);

            // TODO: Produce TransactionDebitFailedEvent()
            throw new BusinessException(e.getMessage());

        } catch (InsufficientBalanceException e) {

            transaction.setTransactionStatus(TransactionStatus.FAILED);
            transactionRepository.save(transaction);

            // TODO: Produce TransactionDebitFailedEvent()
            throw new InsufficientBalanceException(e.getMessage());
        }
    }


    @Transactional(rollbackOn = {BusinessException.class})
    public TransactionDTO depositMoney(TransactionRequest request) {
        BigDecimal feesAmount = feesService.calculateFees(
                TransactionType.DEPOSIT,
                request.amount()
        );

        log.info("Fees amount :: {}", feesAmount);

        Transaction transaction = Transaction.builder()
                .accountNumber(request.accountNumber())
                .toAccountNumber(null)
                .feesAmount(feesAmount.subtract(request.amount()))
                .reference(TransactionUtil.generateTransactionRef(10))
                .amount(feesAmount)
                .transactionType(TransactionType.DEPOSIT)
                .transactionStatus(TransactionStatus.SUCCESS)
                .build();

        transactionRepository.saveAndFlush(transaction);

        try {
            SuccessResponse findAccountResponse = accountClient.findByAccountNumber(request.accountNumber());
            if (findAccountResponse.getCode() != HttpStatus.OK.value())
                throw new BusinessException("Account not found");

            AccountDTO accountDTO = objectMapper.convertValue(findAccountResponse.getData(), AccountDTO.class);
            SuccessResponse successResponse = accountClient.creditAccount(
                    new AccountTransactionRequest(
                            accountDTO.getAccountId(),
                            feesAmount
                    )
            );

            if (successResponse.getCode() != HttpStatus.OK.value())
                throw new BusinessException("Account not credited");

            // TODO: Produce TransactionDebitSuccessEvent()
            return transactionMapper.toDTO(transaction);

        } catch (BusinessException e) {

            transaction.setTransactionStatus(TransactionStatus.FAILED);
            transactionRepository.save(transaction);

            // TODO: Produce TransactionDebitFailedEvent()
            throw new BusinessException(e.getMessage());

        }
    }


    /**
     * Find all transactions.
     *
     * @param pageable Pageable Request (page and size).
     * @return Object Page of TransactionDTO.
     */
    public Page<TransactionDTO> findAll(Pageable pageable) {
        return transactionRepository.findAll(pageable).map(transactionMapper::toDTO);
    }

}
