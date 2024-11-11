package com.example.test.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class HouseTypeDto {

    private String houseType;
    private String description;

    public HouseTypeDto() {
    }

}
