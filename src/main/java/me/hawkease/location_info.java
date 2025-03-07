package me.hawkease;

import java.util.Objects;

public class location_info {
    private double latitude;
    private double longitude;

    public location_info(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        location_info other = (location_info) obj;
        return other.getLatitude() == this.getLatitude() && other.getLongitude() == this.getLongitude();
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude,longitude);
    }
}