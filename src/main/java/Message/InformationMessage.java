package Message;

import java.util.Map;

public class InformationMessage<T> extends TelemetryMessage<T>{
    public InformationMessage(String type, long timestamp, T data) {
        super( timestamp,type,  data);
    }

    public InformationMessage() {
    }

    public InformationMessage(String type, T data) {
        super(type, data);
    }
}
