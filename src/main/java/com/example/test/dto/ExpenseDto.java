package com.example.test.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ExpenseDto {

    private String expenseNo;
    private String description;
    private double amount;
    private String maintenanceRequestNo;

}
