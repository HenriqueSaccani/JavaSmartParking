package Utils;

public class GpsDescriptor {
    private float latitude;
    private float longitude;
    private float high;

    public GpsDescriptor(float latitude, float longitude, float high) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.high = high;
    }

    public GpsDescriptor(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.high=0; //sea level
    }

    public GpsDescriptor() {
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getHigh() {
        return high;
    }

    public void setHigh(float high) {
        this.high = high;
    }

    @Override
    public String toString() {
        return "GpsDescriptor{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", high=" + high +
                '}';
    }
}
