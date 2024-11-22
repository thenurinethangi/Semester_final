package com.example.test.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class EmployeeDto {

    private String employeeId;
    private String name;
    private String address;
    private String phoneNo;
    private double basicSalary;
    private double allowances;
    private String dob;
    private String position;

}
