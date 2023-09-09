package Message;

import Utils.AvailabilityControlDescriptor;

public class ParkingAvailabilityControlMessage extends TelemetryMessage<AvailabilityControlDescriptor>{
    public ParkingAvailabilityControlMessage(String type, AvailabilityControlDescriptor dataValue) {
        super(type, dataValue);
    }

    public ParkingAvailabilityControlMessage(long timestamp, String type, AvailabilityControlDescriptor dataValue) {
        super(timestamp, type, dataValue);
    }

    public ParkingAvailabilityControlMessage() {
    }
}
