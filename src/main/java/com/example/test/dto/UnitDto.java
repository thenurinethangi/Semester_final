package com.example.test.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class UnitDto {

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

    public UnitDto() {

    }

    public String toString(){
        return "House ID: " + houseId + "\nBedRooms: " + bedroom + "\nBathRooms: " + bathroom + "\nSecurity Payment: " + securityCharge + "\nMonthly Rent: " + monthlyRent + "\nHouseType: " + houseType + "\nFloor No: " + floorNo;
    }
}



