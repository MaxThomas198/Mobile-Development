package com.Parking_Mobile_App_Group3.api.models;

public class ParkingLot {
    String id;
    int numberParkingSpaces;
    int numberReservedSpaces;

    public ParkingLot(int numberParkingSpaces){
        this.numberParkingSpaces = numberParkingSpaces;
        this.numberReservedSpaces = 0;
    }
}
