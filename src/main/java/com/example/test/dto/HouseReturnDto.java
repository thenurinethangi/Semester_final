package com.example.test.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class HouseReturnDto {

    private String tenantId;
    private String houseId;
    private String agreementId;
    private String reasonToLeave;
}
