package com.ortega.transaction.transaction;

import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public Transaction toTransaction(TransactionRequest request) {
        if (request == null) return null;

        return new Transaction();
    }

    public TransactionDTO toDTO(Transaction transaction) {
        if (transaction == null) return null;

        return TransactionDTO.builder()
                .transactionId(transaction.getTransactionId())
                .amount(transaction.getAmount())
                .transactionType(transaction.getTransactionType())
                .reference(transaction.getReference())
                .status(transaction.getTransactionStatus())
                .feesAmount(transaction.getFeesAmount())
                .transactionDate(transaction.getTransactionDate())
                .build();
    }

}
