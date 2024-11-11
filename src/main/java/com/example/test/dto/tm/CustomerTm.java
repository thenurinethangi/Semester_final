package com.example.test.dto.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CustomerTm {

    private String customerId;
    private String name;
    private String nic;
    private String address;
    private String phoneNo;
    private String jobTitle;
    private String livingArrangement;
}
