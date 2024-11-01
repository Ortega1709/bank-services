package com.ortega.transaction.fees;

import com.ortega.transaction.exception.FeesNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
        return feesMapper.toDTO(feesRepository.save(fees));
    }

    /**
     * Update existing fees.
     *
     * @param request Object that contains information about fees.
     * @throws FeesNotFoundException If fees not found.
     * @return Object FeesTDO updated.
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


}
