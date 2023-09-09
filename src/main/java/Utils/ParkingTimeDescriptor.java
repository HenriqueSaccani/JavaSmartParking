package Utils;
//take track of the last timestamp and if the parking was or not occupied
public class ParkingTimeDescriptor {
    private String topic;
    private long timestamp;
    private String ledColor;

    public ParkingTimeDescriptor(String topic, long timestamp, String ledColor) {
        this.topic = topic;
        this.timestamp = timestamp;
        this.ledColor = ledColor;
    }

    public ParkingTimeDescriptor() {
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getLedColor() {
        return ledColor;
    }

    public void getLedColor(String ledColor) {
        this.ledColor = ledColor;
    }

    @Override
    public String toString() {
        return "ParkingTimeDescriptor{" +
                "topic='" + topic + '\'' +
                ", timestamp=" + timestamp +
                ", occupied=" + ledColor +
                '}';
    }
}
