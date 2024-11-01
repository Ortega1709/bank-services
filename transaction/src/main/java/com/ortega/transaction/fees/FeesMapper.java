package com.ortega.transaction.fees;

import org.springframework.stereotype.Component;

@Component
public class FeesMapper {

    /**
     * Map FeesRequest to Fees
     *
     * @param request Object that contains information about fees.
     * @return Fees Object.
     */
    public Fees toFees(FeesRequest request) {
        if (request == null) return null;

        return Fees.builder()
                .feesId(request.feesId())
                .maxAmount(request.maxAmount())
                .minAmount(request.minAmount())
                .percent(request.percent())
                .transactionType(request.transactionType())
                .build();
    }

    /**
     * Map Fees to Fees data transfer object.
     *
     * @param fees Object that contains information of fees.
     * @return Fees Data transfer object.
     */
    public FeesDTO toDTO(Fees fees) {
        if (fees == null) return null;

        return FeesDTO.builder()
                .feesId(fees.getFeesId())
                .maxAmount(fees.getMaxAmount())
                .minAmount(fees.getMinAmount())
                .percent(fees.getPercent())
                .transactionType(fees.getTransactionType())
                .createdAt(fees.getCreatedAt())
                .updatedAt(fees.getUpdatedAt())
                .build();
    }
}
