package com.ortega.account.account;

import com.ortega.account.event.account.AccountCreatedEvent;
import com.ortega.account.event.account.AccountCreationFailedEvent;
import com.ortega.account.event.customer.CustomerCreatedEvent;
import com.ortega.account.exception.AccountAlreadyExistsException;
import com.ortega.account.exception.AccountNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final AccountKafkaProducer accountKafkaProducer;

    @Transactional
    public AccountDTO createAccount(CustomerCreatedEvent event) {
        try {
            if (accountRepository.existsByCustomerId(event.getCustomerId())) {
                throw new AccountAlreadyExistsException(
                        String.format("Account with customer ID %s already exists", event.getCustomerId())
                );
            }

            Account account = Account.builder()
                    .customerId(event.getCustomerId())
                    .accountNumber(this.generateAccountNumber())
                    .status(Boolean.TRUE)
                    .build();

            accountRepository.saveAndFlush(account);
            accountKafkaProducer.produceAccountCreatedEvent(
                    new AccountCreatedEvent(
                            account.getAccountNumber(),
                            event.getFirstName(),
                            event.getLastName(),
                            account.getPin()
                    )
            );

            return accountMapper.toDTO(account);
        } catch (Exception e) {
            accountKafkaProducer.produceAccountCreationFailedEvent(
                    new AccountCreationFailedEvent(event.getCustomerId())
            );
            return null;
        }
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
