package com.ortega.transaction.fees;

import com.ortega.transaction.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fees")
public class FeesController {

    private final FeesService feesService;

    /**
     * Handle create new fees request.
     *
     * @param request Object that contains fees information.
     * @return Object SuccessResponse of FeesDTO.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse createFee(@RequestBody FeesRequest request) {
        FeesDTO feesDTO = feesService.createFees(request);

        return SuccessResponse.builder()
                .status("success")
                .code(HttpStatus.CREATED.value())
                .message("Fee created successfully")
                .data(feesDTO)
                .build();
    }

    /**
     * Handle update fees request.
     *
     * @param request Object that contains fees information.
     * @return Object SuccessResponse of FeesDTO.
     */
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponse updateFee(@RequestBody FeesRequest request) {
        FeesDTO feesDTO = feesService.updateFees(request);

        return SuccessResponse.builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Fee updated successfully")
                .data(feesDTO)
                .build();
    }

    /**
     * Handle find all fees request.
     *
     * @param page which page.
     * @param size size of page.
     * @return Object SuccessResponse of FeesDTO.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponse findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<FeesDTO> feesDTO = feesService.findAll(pageable);

        return SuccessResponse.builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Fees found")
                .data(feesDTO)
                .build();
    }

    /**
     * Handle delete fee request.
     *
     * @param feesId unique key
     * @return Object SuccessResponse of FeesTDO.
     */
    @DeleteMapping("/{feesId}")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponse deleteFee(@PathVariable("feesId") UUID feesId) {
        feesService.deleteFeesById(feesId);

        return SuccessResponse.builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Fee deleted successfully")
                .data(null)
                .build();
    }
}
