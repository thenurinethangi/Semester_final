package com.example.test.dto.tm;

public class FloorTm {

    private int floorNo;
    private int noOfHouses;

    public FloorTm(int floorNo, int noOfHouses) {
        this.floorNo = floorNo;
        this.noOfHouses = noOfHouses;
    }

    public FloorTm() {
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
