package com.ortega.account.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    boolean existsByCustomerId(UUID customerId);
    Optional<Account> findByAccountNumber(String customerId);
}
