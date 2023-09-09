package Message;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TelemetryMessage<t> {
    @JsonProperty("timestamp")
    private long timestamp;

    @JsonProperty("type")
    private String type;

    @JsonProperty("data")
    private t dataValue;

    public TelemetryMessage(String type, t dataValue) {
        this.type = type;
        this.dataValue = dataValue;
        this.timestamp=System.currentTimeMillis();
    }

    public TelemetryMessage(long timestamp, String type, t dataValue) {
        this.timestamp = timestamp;
        this.type = type;
        this.dataValue = dataValue;
    }

    public TelemetryMessage() {
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public t getDataValue() {
        return dataValue;
    }

    public void setDataValue(t dataValue) {
        this.dataValue = dataValue;
    }

    @Override
    public String toString() {
        return "TelemetryMessage{" +
                "timestamp=" + timestamp +
                ", type='" + type + '\'' +
                ", dataValue=" + dataValue +
                '}';
    }
}
