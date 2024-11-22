package com.example.test.dto.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OwnerTm {

    private String ownerId;
    private String name;
    private String phoneNo;
    private int membersCount;
    private String purchaseDate;
    private String houseId;
    private String invoiceNo;

}
