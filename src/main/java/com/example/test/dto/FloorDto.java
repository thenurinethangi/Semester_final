package com.example.test.dto;

public class FloorDto {

    private int floorNo;
    private int noOfHouses;

    public FloorDto(int floorNo, int noOfHouses) {
        this.floorNo = floorNo;
        this.noOfHouses = noOfHouses;
    }

    public FloorDto() {
    }

    public int getFloorNo() {
        return floorNo;
    }

    public void setFloorNo(int floorNo) {
        this.floorNo = floorNo;
    }

    public int getNoOfHouses() {
        return noOfHouses;
    }

    public void setNoOfHouses(int noOfHouses) {
        this.noOfHouses = noOfHouses;
    }
}
