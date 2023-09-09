package Utils;

public class AvailabilityControlDescriptor {
    private boolean unavailable; //if True the parking must be Unavailable, if false the parking will be able to be used again

    private String parkingId;

    public AvailabilityControlDescriptor(boolean unavailable, String parkingId) {
        this.unavailable = unavailable;
        this.parkingId = parkingId;
    }

    public AvailabilityControlDescriptor() {
    }

    public boolean isUnavailable() {
        return unavailable;
    }

    public void setUnavailable(boolean unavailable) {
        this.unavailable = unavailable;
    }

    public String getParkingId() {
        return parkingId;
    }

    public void setParkingId(String parkingId) {
        this.parkingId = parkingId;
    }
}
