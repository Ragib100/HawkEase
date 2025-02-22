package me.hawkease;

public class LocationInfo {
    private double latitude;
    private double longitude;
    private String rentPrice;

    public LocationInfo(double latitude, double longitude, String rentPrice) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.rentPrice = rentPrice;
    }

    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getRentPrice() { return rentPrice; }
}