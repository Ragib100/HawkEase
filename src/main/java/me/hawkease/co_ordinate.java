package me.hawkease;

public record co_ordinate(double lat, double lon) {

    public boolean is_less_than(co_ordinate other) {
        if(this.lat!=other.lat) return this.lat<other.lat;
        return this.lon<other.lon;
    }

    public boolean is_equal(co_ordinate other) {
        return this.lat==other.lat && this.lon==other.lon;
    }
}
