package com.example.test.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OwnerDto {

    private String ownerId;
    private String name;
    private String phoneNo;
    private int membersCount;
    private String purchaseDate;
    private String houseId;
    private String invoiceNo;
    private String email;
}
