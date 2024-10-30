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
import java.math.RoundingMode;
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

    /**
     * Create a new customer account.
     *
     * @param request Object that contains account information.
     * @return AccountDTO that contains account created information.
     * @throws AccountAlreadyExistsException if an account already exists by customerId.
     */
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

    /**
     * Retrieve account information.
     *
     * @param accountNumber Unique accountNumber.
     * @return AccountDTO that contains account information.
     * @throws AccountNotFoundException if its doesn't find.
     */
    public AccountDTO findByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber).map(accountMapper::toDTO)
                .orElseThrow(
                        () -> new AccountNotFoundException(
                                String.format("Account with number %s not found", accountNumber)
                        )
                );
    }

    /**
     * Find All accounts
     *
     * @param pageable PageRequest to return size and page of accounts.
     * @return Pageable AccountDTO.
     */
    public Page<AccountDTO> findAll(Pageable pageable) {
        return accountRepository.findAll(pageable).map(accountMapper::toDTO);
    }

    /**
     * Delete an existing account.
     *
     * @param customerId UUID identifier associate to account.
     */
    @Transactional
    public void deleteAccountByCustomerId(UUID customerId) {
        log.info("Deleting account by customer id :: {}", customerId);

        accountRepository.deleteByCustomerId(customerId);
    }

    /**
     * Update account status to True or False.
     *
     * @param accountId UUID identifier of an account.
     * @param status    Boolean True or False.
     * @return AccountDTO object that contains account status updated.
     * @throws BusinessException if request to find client information failed.
     */
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

    /**
     * Credit an existing account.
     *
     * @param request Object contains accountId and Amount to credit.
     * @return AccountDTO that contains account credited information.
     */
    @Transactional
    public AccountDTO creditAccount(AccountTransactionRequest request) {
        if (request.amount().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Credit amount must be positive");

        Account account = findAccountById(request.accountId());
        account.setBalance(account.getBalance().add(request.amount()));
        return accountMapper.toDTO(accountRepository.save(account));
    }

    /**
     * Debit an existing account.
     *
     * @param request Object contains accountId and Amount to debit.
     * @return AccountDTO that contains account debited information.
     * @throws IllegalArgumentException     if credit amounts aren't positive.
     * @throws InsufficientBalanceException if account balance isn't bigger than debited amount.
     */
    @Transactional
    public AccountDTO debitAccount(AccountTransactionRequest request) {
        if (request.amount().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Credit amount must be positive");


        Account account = findAccountById(request.accountId());
        if (account.getBalance().compareTo(request.amount()) < 0)
            throw new InsufficientBalanceException(
                    String.format(
                            "Insufficient balance for debit transaction %.2f",
                            request.amount().setScale(2, RoundingMode.HALF_UP)
                    )
            );

        account.setBalance(account.getBalance().subtract(request.amount()));
        return accountMapper.toDTO(accountRepository.save(account));
    }

    /**
     * Retrieve account information.
     *
     * @param accountId unique ID of account.
     * @return Account object that contains information.
     * @throws AccountNotFoundException If its doesn't find.
     */
    private Account findAccountById(UUID accountId) {
        return accountRepository.findById(accountId).orElseThrow(
                () -> new AccountNotFoundException(
                        String.format("Account with id %s not found", accountId)
                )
        );
    }

    /**
     * Generate account number for new customer.
     *
     * @return The generated account number.
     */
    private String generateAccountNumber() {
        String accountPrefix = "ACC";
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        int randomPart = new Random().nextInt(100000);

        return accountPrefix + datePart + String.format("%05d", randomPart);
    }

    /**
     * Generate random pin for new customer.
     *
     * @return A random pin of six digits.
     */
    private Integer generateRandomPin() {
        int generatedPin = new Random().nextInt(1000000);
        return Integer.valueOf(String.format("%06d", generatedPin));
    }
}
