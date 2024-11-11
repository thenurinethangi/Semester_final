package com.example.test.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class UnitTm {

    private String houseId;
    private int bedroom;
    private int bathroom;
    private String rentOrBuy;
    private String totalValue;
    private String securityCharge;
    private String monthlyRent;
    private String status;
    private String houseType;
    private int floorNo;

    public UnitTm() {

    }
}


