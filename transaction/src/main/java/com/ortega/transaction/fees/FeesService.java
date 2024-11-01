package com.ortega.transaction.fees;

import com.ortega.transaction.exception.FeesNotFoundException;
import com.ortega.transaction.transaction.TransactionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeesService {

    private final FeesMapper feesMapper;
    private final FeesRepository feesRepository;

    /**
     * Create new fees.
     *
     * @param request Object that contains information about fees to create.
     * @return Object FeesDTO of fees created.
     */
    public FeesDTO createFees(FeesRequest request) {
        Fees fees = feesMapper.toFees(request);
        feesRepository.saveAndFlush(fees);

        return feesMapper.toDTO(fees);
    }

    /**
     * Update existing fees.
     *
     * @param request Object that contains information about fees.
     * @return Object FeesTDO updated.
     * @throws FeesNotFoundException If fees not found.
     */
    public FeesDTO updateFees(FeesRequest request) {
        feesRepository.findById(request.feesId()).orElseThrow(
                () -> new FeesNotFoundException(
                        String.format("Fees with id %s not found", request.feesId())
                )
        );

        Fees fees = feesMapper.toFees(request);
        return feesMapper.toDTO(feesRepository.save(fees));
    }

    /**
     * Retrieve all fees paged.
     *
     * @param pageable PageRequest with page and size of page.
     * @return Object Page of FeesDTO.
     */
    public Page<FeesDTO> findAll(Pageable pageable) {
        return feesRepository.findAll(pageable).map(feesMapper::toDTO);
    }

    /**
     * Delete an existing fees.
     *
     * @param feesId unique key
     */
    public void deleteFeesById(UUID feesId) {
        feesRepository.deleteById(feesId);
    }

    /**
     * Calculate fees of transaction.
     *
     * @param transactionType (WITHDRAW, DEPOSIT, TRANSFER)
     * @param amount
     * @return Fees to apply on transaction.
     */
    public BigDecimal calculateFees(TransactionType transactionType, BigDecimal amount) {
        Optional<Fees> feesOptional = feesRepository.findFeesByTransactionTypeAndAmountWithinRange(
                transactionType.name(),
                amount
        );

        return feesOptional.map(
                fees -> amount.add(
                        amount.multiply(
                                BigDecimal.valueOf(fees.getPercent())).divide(
                                BigDecimal.valueOf(100)
                        )
                )
        ).orElse(amount.add(BigDecimal.ZERO)); // If fees with transactionType and amount is not found, No transaction fees apply.
    }

}
