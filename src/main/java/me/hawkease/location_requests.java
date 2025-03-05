package me.hawkease;

public record location_requests(String email, double lat, double lon) implements Comparable<location_requests> {
    @Override
    public int compareTo(location_requests other) {
        // If latitudes are different, compare latitudes
        if (this.lat != other.lat) {
            return Double.compare(this.lat, other.lat);
        }
        // If latitudes are the same, compare longitudes
        return Double.compare(this.lon, other.lon);
    }
}