package com.ortega.account.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ortega.account.customer.CustomerClient;
import com.ortega.account.customer.CustomerDTO;
import com.ortega.account.event.account.AccountStatusUpdatedEvent;
import com.ortega.account.exception.AccountAlreadyExistsException;
import com.ortega.account.exception.AccountNotFoundException;
import com.ortega.account.exception.BusinessException;
import com.ortega.account.exception.InsufficientBalanceException;
import com.ortega.account.response.SuccessResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final ObjectMapper objectMapper;
    private final AccountMapper accountMapper;
    private final CustomerClient customerClient;
    private final AccountRepository accountRepository;
    private final AccountKafkaProducer accountKafkaProducer;

    @Transactional
    public AccountDTO createAccount(AccountRequest request) {
        log.info("Creating account for customer :: {}", request.customerId());

        if (accountRepository.existsByCustomerId(request.customerId())) {
            throw new AccountAlreadyExistsException(
                    String.format("Account with customer ID %s already exists", request.customerId())
            );
        }

        Account account = Account.builder()
                .customerId(request.customerId())
                .accountNumber(this.generateAccountNumber())
                .pin(this.generateRandomPin())
                .balance(BigDecimal.ZERO)
                .status(Boolean.TRUE)
                .build();

        accountRepository.saveAndFlush(account);
        return accountMapper.toDTO(account);
    }

    public AccountDTO findByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber).map(accountMapper::toDTO)
                .orElseThrow(
                        () -> new AccountNotFoundException(
                                String.format("Account with number %s not found", accountNumber)
                        )
                );
    }

    public Page<AccountDTO> findAll(Pageable pageable) {
        return accountRepository.findAll(pageable).map(accountMapper::toDTO);
    }

    @Transactional
    public void deleteAccountByCustomerId(UUID customerId) {
        log.info("Deleting account by customer id :: {}", customerId);

        accountRepository.deleteByCustomerId(customerId);
    }


    @Transactional
    public AccountDTO updateAccountStatusById(UUID accountId, Boolean status) {
        String action = status ? "Activating" : "Deactivating";
        log.info("{} account status :: {}", action, accountId);

        Account account = findAccountById(accountId);
        account.setStatus(status);

        accountRepository.saveAndFlush(account);
        SuccessResponse successResponse = customerClient.findCustomerById(account.getCustomerId());

        if (successResponse.getCode() != HttpStatus.OK.value())
            throw new BusinessException("Something went wrong");

        CustomerDTO customerDTO = objectMapper.convertValue(successResponse.getData(), CustomerDTO.class);
        accountKafkaProducer.produceAccountStatusUpdatedEvent(
                new AccountStatusUpdatedEvent(
                        customerDTO.getFirstName(),
                        customerDTO.getLastName(),
                        customerDTO.getEmail(),
                        status
                )
        );

        return accountMapper.toDTO(account);
    }

    @Transactional
    public AccountDTO creditAccount(AccountTransactionRequest request) {
        if (request.amount().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Credit amount must be positive");

        Account account = findAccountById(request.accountId());
        account.setBalance(account.getBalance().add(request.amount()));
        return accountMapper.toDTO(accountRepository.save(account));
    }

    @Transactional
    public AccountDTO debitAccount(AccountTransactionRequest request) {
        if (request.amount().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Credit amount must be positive");


        Account account = findAccountById(request.accountId());
        if (account.getBalance().compareTo(request.amount()) < 0)
            throw new InsufficientBalanceException(
                    String.format("Insufficient balance for debit transaction %f", request.amount())
            );

        account.setBalance(account.getBalance().subtract(request.amount()));
        return accountMapper.toDTO(accountRepository.save(account));
    }

    private Account findAccountById(UUID accountId) {
        return accountRepository.findById(accountId).orElseThrow(
                () -> new AccountNotFoundException(
                        String.format("Account with id %s not found", accountId)
                )
        );
    }

    private String generateAccountNumber() {
        String accountPrefix = "ACC";

        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        int randomPart = new Random().nextInt(100000);

        return accountPrefix + datePart + String.format("%05d", randomPart);
    }

    private Integer generateRandomPin() {
        int generatedPin = new Random().nextInt(1000000);
        return Integer.valueOf(String.format("%06d", generatedPin));
    }
}
