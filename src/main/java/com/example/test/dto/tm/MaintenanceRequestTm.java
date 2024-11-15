package com.example.test.dto.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MaintenanceRequestTm {

    private String maintenanceRequestNo;
    private String description;
    private double estimatedCost;
    private String actualCost;
    private String date;
    private String assignedTechnician;
    private String tenantId;
    private String status;

}
