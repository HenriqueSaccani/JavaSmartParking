package Message;

import Utils.ParkingDescriptor;

import java.util.Map;

public class RegisterMessage extends TelemetryMessage<ParkingDescriptor>{

    public RegisterMessage(String type, ParkingDescriptor data) {
        super(type,data);
    }

    public RegisterMessage(long timestamp, String type, ParkingDescriptor dataValue) {
        super(timestamp, type, dataValue);
    }

    public RegisterMessage() {

    }

}
