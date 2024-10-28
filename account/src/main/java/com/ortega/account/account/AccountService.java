package com.ortega.account.account;

import com.ortega.account.event.account.AccountCreatedEvent;
import com.ortega.account.event.account.AccountCreationFailedEvent;
import com.ortega.account.event.customer.CustomerCreatedEvent;
import com.ortega.account.exception.AccountAlreadyExistsException;
import com.ortega.account.exception.AccountNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

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
