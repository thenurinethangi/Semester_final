package com.example.test.dto.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ExpenseTm {

    private String expenseNo;
    private String description;
    private double amount;
    private String requestNo;
    private String date;

}
