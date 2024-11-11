package com.example.test.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class RequestDto {

    private String requestId;
    private String customerId;
    private String date;
    private String rentOrBuy;
    private String houseType;
    private int noOfFamilyMembers;
    private double monthlyIncome;
    private double annualIncome;
    private String bankAccountDetails;
    private String reasonForLeaving;
    private String estimatedMonthlyBudgetForRent;
    private String leaseTurnDesire;
    private String previousLandlordNumber;
    private String isSmoking;
    private String hasCriminalBackground;
    private String hasPets;
    private String allDocumentsProvided;
    private String qualifiedCustomerOrNot;
    private String agreesToAllTermsAndConditions;
    private String isPaymentsCompleted;
    private String customerRequestStatus;
    private String requestStatus;
    private String houseId;

    public RequestDto(String requestId, String customerId, String date, String rentOrBuy, String houseType, int noOfFamilyMembers, double monthlyIncome, double annualIncome, String bankAccountDetails, String reasonForLeaving, String estimatedMonthlyBudgetForRent, String leaseTurnDesire, String previousLandlordNumber, String isSmoking, String hasCriminalBackground, String hasPets) {
        this.requestId = requestId;
        this.customerId = customerId;
        this.date = date;
        this.rentOrBuy = rentOrBuy;
        this.houseType = houseType;
        this.noOfFamilyMembers = noOfFamilyMembers;
        this.monthlyIncome = monthlyIncome;
        this.annualIncome = annualIncome;
        this.bankAccountDetails = bankAccountDetails;
        this.reasonForLeaving = reasonForLeaving;
        this.estimatedMonthlyBudgetForRent = estimatedMonthlyBudgetForRent;
        this.leaseTurnDesire = leaseTurnDesire;
        this.previousLandlordNumber = previousLandlordNumber;
        this.isSmoking = isSmoking;
        this.hasCriminalBackground = hasCriminalBackground;
        this.hasPets = hasPets;
    }
}





