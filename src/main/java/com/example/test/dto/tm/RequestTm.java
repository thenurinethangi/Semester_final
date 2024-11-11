package com.example.test.dto.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class RequestTm {

    private String requestId;
    private String customerId;
    private String rentOrBuy;
    private String houseType;
    private String leaseTurnDesire;
    private String allDocumentsProvided;
    private String qualifiedCustomerOrNot;
    private String agreesToAllTermsAndConditions;
    private String isPaymentsCompleted;
    private String customerRequestStatus;
    private String requestStatus;
    private String houseId;
}
