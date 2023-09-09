package Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class ParkingLotInformationDescriptor {
    private long averageParkingTimeInMilli;

    private ArrayList<String> freeParkingSpots;

    private HashMap<String, ParkingDescriptor> totalParkingSpots;

    public ParkingLotInformationDescriptor() {
    }

    public ParkingLotInformationDescriptor(long averageParkingTimeInMilli, ArrayList<String> freeParkingSpots,HashMap<String, ParkingDescriptor>totalParkingSpots) {
        this.averageParkingTimeInMilli = averageParkingTimeInMilli;
        this.freeParkingSpots = freeParkingSpots;
        this.totalParkingSpots = totalParkingSpots;
    }

    public ParkingLotInformationDescriptor(ArrayList<String>  freeParkingSpots, HashMap<String, ParkingDescriptor> totalParkingSpots) {
        this.freeParkingSpots = freeParkingSpots;
        this.totalParkingSpots = totalParkingSpots;
    }

    public long getAverageParkingTimeInMilli() {
        return averageParkingTimeInMilli;
    }

    public void setAverageParkingTimeInMilli(long averageParkingTimeInMilli) {
        this.averageParkingTimeInMilli = averageParkingTimeInMilli;
    }

    public ArrayList<String> getFreeParkingSpots() {
        return freeParkingSpots;
    }

    public void setFreeParkingSpots(ArrayList<String> freeParkingSpots) {
        this.freeParkingSpots = freeParkingSpots;
    }

    public HashMap<String, ParkingDescriptor> getTotalParkingSpots() {
        return totalParkingSpots;
    }

    public void setTotalParkingSpots(HashMap<String, ParkingDescriptor> totalParkingSpots) {
        this.totalParkingSpots = totalParkingSpots;
    }

    public void addParkingSpot(String topic, ParkingDescriptor parkingDescriptor){
        this.totalParkingSpots.put(topic,parkingDescriptor);
    }

    public void removeParkingSpot(String topic){
        this.totalParkingSpots.remove(topic);
    }

    public void addFreeParkingSpot(String topic){
        this.freeParkingSpots.add(topic);
    }

    public void removeFreeParkingSpot(String topic){
        this.freeParkingSpots.remove(topic);
    }

    public boolean isParkingFree(String topic){
        if(freeParkingSpots.contains(topic)){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "ParkingLotInformationDescriptor{" +
                "averageParkingTimeInMilli=" + averageParkingTimeInMilli +
                ", freeParkingSpots=" + freeParkingSpots +
                ", totalParkingSpots=" + totalParkingSpots +
                '}';
    }
}
