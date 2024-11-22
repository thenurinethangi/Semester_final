package com.example.test.dto.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class PurchaseAgreementTm {

    private String purchaseAgreementId;
    private String homeOwnerId;
    private String houseId;
    private double purchasePrice;
    private String signedDate;
    private String status;
}
