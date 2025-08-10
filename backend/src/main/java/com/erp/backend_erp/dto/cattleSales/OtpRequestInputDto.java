package com.erp.backend_erp.dto.cattleSales;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OtpRequestInputDto {
    private Long requestedByUserId;
    private Long approverUserId;
}
