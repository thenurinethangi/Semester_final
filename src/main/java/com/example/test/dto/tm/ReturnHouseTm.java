package com.example.test.dto.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ReturnHouseTm {

    private String returnNo;
    private String reason;
    private String date;
    private String tenantId;
    private String houseId;
    private String refundedAmount;
    private String expenseNo;

}
