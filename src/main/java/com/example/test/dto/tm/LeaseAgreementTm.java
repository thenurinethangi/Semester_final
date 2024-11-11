package com.example.test.dto.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class LeaseAgreementTm {

    private String leaseId;
    private String tenantId;
    private String houseId;
    private String leaseTurn;
    private String startDate;
    private String endDate;
    private String status;
}
