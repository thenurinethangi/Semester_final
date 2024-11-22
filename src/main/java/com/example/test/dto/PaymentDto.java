package com.example.test.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PaymentDto {

    private String invoiceNo;
    private double amount;
    private String date;
    private String paymentType;
    private String tenantId;
}
