package com.example.test.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class LeaseAgreementDto {

    private String leaseId;
    private String tenantId;
    private String houseId;
    private String leaseTurn;
    private String startDate;
    private String endDate;
    private String status;
    private double monthlyRent;
    private double securityPayment;
}
