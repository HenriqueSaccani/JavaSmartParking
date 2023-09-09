package Device;

import Message.ParkingAvailabilityControlMessage;
import Message.TelemetryMessage;
import Utils.AvailabilityControlDescriptor;
import Configuration.SmartParkingConfiguration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExternalServer {
    private static final Logger logger = LoggerFactory.getLogger(ExternalServer.class);
    public static final String BASIC_TOPIC = SmartParkingConfiguration.BASIC_TOPIC;
    public static final String CONTROL_AVAILABILITY_TOPIC = SmartParkingConfiguration.CONTROL_AVAILABILITY_TOPIC;

    public static final String MESSAGE_PARKING_RESERVE_TYPE = SmartParkingConfiguration.MESSAGE_PARKING_RESERVE_TYPE;
    public static final String MESSAGE_PARKING_AVAILABILITY_CONTROL_TYPE = SmartParkingConfiguration.MESSAGE_PARKING_AVAILABILITY_CONTROL_TYPE;

    public int QOS_VALUE = SmartParkingConfiguration.QOS_VALUE;
    private MqttClient mqttClient;
    private ObjectMapper mapper;

    public ExternalServer() {
        mapper = new ObjectMapper();
    }

    public ExternalServer(MqttClient mqttClient) {
        this.mqttClient = mqttClient;
        mapper = new ObjectMapper();
    }

    public void controlParkingUnavailability(String lotId, String parkingId, boolean unavailable){
        try{
            publishTelemetryData(
                    String.format("%s/%s/%s", BASIC_TOPIC, lotId, CONTROL_AVAILABILITY_TOPIC),
                    new ParkingAvailabilityControlMessage(MESSAGE_PARKING_RESERVE_TYPE, new AvailabilityControlDescriptor(unavailable,parkingId))
            );
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
        }
    }

    private void publishTelemetryData(String topic, TelemetryMessage<?> telemetryMessage) throws MqttException, JsonProcessingException {

        logger.info("Sending to topic: {} -> Data: {}", topic, telemetryMessage);

        if (this.mqttClient != null && this.mqttClient.isConnected() && telemetryMessage != null && topic != null) {

            String messagePayload = mapper.writeValueAsString(telemetryMessage);

            MqttMessage mqttMessage = new MqttMessage(messagePayload.getBytes());
            mqttMessage.setQos(QOS_VALUE);

            mqttClient.publish(topic, mqttMessage);

            logger.info("Data Correctly Published to topic: {}", topic);

        } else
            logger.error("Error: Topic or Msg = Null or MQTT Client is not Connected !");
    }

    public MqttClient getMqttClient() {
        return mqttClient;
    }

    public void setMqttClient(MqttClient mqttClient) {
        this.mqttClient = mqttClient;
    }

    @Override
    public String toString() {
        return "ExternalServer{" +
                "QOS_VALUE=" + QOS_VALUE +
                ", mqttClient=" + mqttClient +
                ", mapper=" + mapper +
                '}';
    }
}