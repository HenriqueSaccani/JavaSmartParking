package Utils;

import Resource.GenericResource;
import org.eclipse.paho.client.mqttv3.MqttClient;

import java.util.HashMap;

public class ParkingDescriptor extends Object{
    private String lotId;
    private GpsDescriptor gpsDescriptor;
    private String parkingId;

    public ParkingDescriptor(String lotId,GpsDescriptor gpsDescriptor, String parkingId){ // HashMap <String,GenericResource> resourceHashMap) {
        this.lotId = lotId;
        this.gpsDescriptor = gpsDescriptor;
        this.parkingId = parkingId;
        //this.resourceHashMap = resourceHashMap;
    }

    public ParkingDescriptor() {
    }


    public String getLotId() {
        return lotId;
    }


    public GpsDescriptor getGpsDescriptor() {
        return gpsDescriptor;
    }


    public String getParkingId() {
        return parkingId;
    }

    public void setLotId(String lotId) {
        this.lotId = lotId;
    }

    public void setGpsDescriptor(GpsDescriptor gpsDescriptor) {
        this.gpsDescriptor = gpsDescriptor;
    }


    public void setParkingId(String parkingId) {
        this.parkingId = parkingId;
    }


    @Override
    public String toString() {
        return "ParkingDescriptor{" +
                "lotId='" + lotId + '\'' +
                ", gpsDescriptor=" + gpsDescriptor +
                ", parkingId='" + parkingId + '\'' +
                '}';
    }
}
