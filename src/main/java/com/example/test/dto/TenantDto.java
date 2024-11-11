package com.example.test.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class TenantDto {

    private String tenantId;
    private String name;
    private String phoneNo;
    private int membersCount;
    private String rentStartDate;
    private double monthlyRent;
    private String lastPaidMonth;
    private String houseId;
    private String email;
    private double securityPaymentRemain;
}
