package com.ortega.account.account;

import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    /**
     * Map AccountRequest to Account.
     *
     * @param request
     * @return Object Account.
     */
    public Account toAccount(AccountRequest request) {
        if (request == null) return null;

        return Account.builder()
                .customerId(request.customerId())
                .build();
    }

    /**
     * Map Account to AccountDTO.
     *
     * @param account
     * @return Object AccountDTO.
     */
    public AccountDTO toDTO(Account account) {
        if (account == null) return null;

        return AccountDTO.builder()
                .accountId(account.getAccountId())
                .accountNumber(account.getAccountNumber())
                .customerId(account.getCustomerId())
                .balance(account.getBalance())
                .status(account.getStatus())
                .pin(account.getPin())
                .build();
    }
}
