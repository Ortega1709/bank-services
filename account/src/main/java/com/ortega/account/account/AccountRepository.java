package com.ortega.account.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    /**
     * Exists an account with customerId
     *
     * @param customerId UUID associate to account.
     * @return True or False
     */
    boolean existsByCustomerId(UUID customerId);

    /**
     * Find an existing account.
     *
     * @param accountNumber UUID associate to account.
     * @return Optional Object account.
     */
    Optional<Account> findByAccountNumber(String accountNumber);

    /**
     * Delete account by customerId
     *
     * @param customerId UUID associate to account.
     */
    void deleteByCustomerId(UUID customerId);
}
