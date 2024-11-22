package com.example.test.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MaintenanceRequestDto {

    private String maintenanceRequestNo;
    private String description;
    private double estimatedCost;
    private String actualCost;
    private String date;
    private String assignedTechnician;
    private String tenantId;
    private String status;
}
