package com.example.test.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
public class EmployeeTm {

    private String employeeId;
    private String name;
    private String address;
    private String phoneNo;
    private double basicSalary;
    private double allowances;
    private String dob;
    private String position;

    public EmployeeTm() {
    }

    public EmployeeTm(String employeeId, String name, String address, String phoneNo, double basicSalary, double allowances, String dob, String position) {
        this.employeeId = employeeId;
        this.name = name;
        this.address = address;
        this.phoneNo = phoneNo;
        this.basicSalary = basicSalary;
        this.allowances = allowances;
        this.dob = dob;
        this.position = position;
    }
}
