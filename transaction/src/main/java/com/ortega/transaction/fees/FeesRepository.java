package com.ortega.transaction.fees;

import com.ortega.transaction.transaction.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FeesRepository extends JpaRepository<Fees, UUID> {

    @Query(
            value = "SELECT * FROM fees " +
                    "WHERE transaction_type = :transactionType " +
                    "AND :amount BETWEEN min_amount AND max_amount",
            nativeQuery = true
    )
    Optional<Fees> findFeesByTransactionTypeAndAmountWithinRange(
            @Param("transactionType") String transactionType,
            @Param("amount") BigDecimal amount
    );

}
