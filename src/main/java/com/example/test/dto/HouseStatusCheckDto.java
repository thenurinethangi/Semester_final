package com.example.test.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class HouseStatusCheckDto {

    private String checkNumber;
    private String livingRoomStatus;
    private String kitchenStatus;
    private String bedRoomsStatus;
    private String bathRoomsStatus;
    private String totalHouseStatus;
    private String tenantId;
    private String houseId;
    private String estimatedCostForRepair;
    private String isPaymentDone;
}
