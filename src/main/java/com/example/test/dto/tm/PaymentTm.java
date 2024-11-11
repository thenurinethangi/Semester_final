package com.example.test.dto.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PaymentTm {

    private String invoiceNo;
    private double amount;
    private String date;
    private String paymentType;
    private String tenantId;
}
