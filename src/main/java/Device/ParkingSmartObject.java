package Device;

import Message.ParkingAvailabilityControlMessage;
import Message.RegisterMessage;
import Message.TelemetryMessage;
import Resource.*;
import Configuration.SmartParkingConfiguration;
import Utils.AvailabilityControlDescriptor;
import Utils.GpsDescriptor;
import Utils.ParkingDescriptor;
import Utils.ResourceDataListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Optional;

//example of path : smartparking/lot/mantova001/parking/134891341a4afc/
public class ParkingSmartObject {
    private static final Logger logger = LoggerFactory.getLogger(ParkingSmartObject.class);
    public  static final String BASIC_TOPIC = SmartParkingConfiguration.BASIC_TOPIC;
    public static final String PARKING_TOPIC= SmartParkingConfiguration.PARKING_TOPIC;
    public static final String PRESENCE_SENSOR_TOPIC= SmartParkingConfiguration.PRESENCE_SENSOR_TOPIC;
    public static final String LED_COLOR_TOPIC = SmartParkingConfiguration.LED_COLOR_TOPIC;
    public static final String REGISTER_TOPIC= SmartParkingConfiguration.REGISTER_TOPIC;
    public static final String AVAILABILITY_TOPIC = SmartParkingConfiguration.AVAILABILITY_TOPIC;

    private static final String REGISTER_PARKING_TYPE= SmartParkingConfiguration.MESSAGE_REGISTER_PARKING_TYPE;

    public static final int QOS_REGISTER_VALUE = SmartParkingConfiguration.QOS_REGISTER_VALUE;
    public static final int QOS_VALUE= SmartParkingConfiguration.QOS_VALUE;
    private String lotId;
    private GpsDescriptor gpsDescriptor;
    private MqttClient mqttClient;
    private String parkingId;


    private HashMap <String,GenericResource> resourceHashMap;
    private ObjectMapper mapper;
    private boolean FirstTimeFindingSmartParkingLed=true;
    private ParkingLed parkingLed;
    private ParkingDescriptor parkingDescriptor;
    private boolean unavailable;

    public ParkingSmartObject(){
        resourceHashMap=new HashMap<>();
        this.mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public void init(String lotId, GpsDescriptor gpsDescriptor,MqttClient mqttClient,String parkingId,HashMap<String,GenericResource> resourceHashMap){
        this.lotId = lotId;
        this.gpsDescriptor = gpsDescriptor;
        this.resourceHashMap = resourceHashMap;
        this.mqttClient = mqttClient;
        this.parkingId=parkingId;
        this.unavailable = false;

        parkingDescriptor = new ParkingDescriptor(lotId,gpsDescriptor,parkingId);
        logger.info("Initializing parking smart object parkingId {}",parkingId);

    }
    public void start() throws InterruptedException {
        logger.info("Starting ParkingIOT Smart object");
        ParkingRegister();
        ResourceRegister();
        SubscribeToAvailabilityChannel();
    }

    public void ResourceRegister(){
        try{
            this.resourceHashMap.forEach((key, value) -> {
                if (key != null && value != null) {
                    GenericResource genericResource = value;

                    logger.info("Registering to Resource {} notifications ...", genericResource.getType());

                    //registering normaParkingLed
                    if (genericResource.getType().equals(ParkingLed.RESOURCE_TYPE)){
                        ParkingLed parkingLed = (ParkingLed)genericResource;
                        if (FirstTimeFindingSmartParkingLed) {
                            try {
                                this.parkingLed = parkingLed;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            FirstTimeFindingSmartParkingLed = false;
                        }
                        parkingLed.addDataListener(new ResourceDataListener<String>() {
                            @Override
                            public void onDataChanged(GenericResource<String> resource, String updatedValue) {
                                try {
                                    publishTelemetryData(
                                            String.format("%s/%s/%s/%s/%s", BASIC_TOPIC, lotId,PARKING_TOPIC,parkingId, LED_COLOR_TOPIC),
                                            new TelemetryMessage<>(parkingLed.getType(),updatedValue));
                                } catch (MqttException | JsonProcessingException e) {
                                    e.getLocalizedMessage();
                                }
                            }
                        });
                    }

                    if (genericResource.getType().equals(GenericBooleanPresenceSensorResource.RESOURCE_TYPE)){
                        AbstractPresenceSensor abstractPresenceSensor = (AbstractPresenceSensor)genericResource;
                        if (abstractPresenceSensor.isBoolean()){

                            GenericBooleanPresenceSensorResource genericBooleanPresenceSensorResource = (GenericBooleanPresenceSensorResource) abstractPresenceSensor;

                            value.addDataListener(new ResourceDataListener() {
                                @Override
                                public void onDataChanged(GenericResource resource, Object updatedValue) {
                                    try {
                                        logger.info("Presence Sensor with id {} now is  {}",resource.getId(), updatedValue);
                                        publishTelemetryData(
                                                String.format("%s/%s/%s/%s/%s", BASIC_TOPIC, lotId, PARKING_TOPIC, parkingId, PRESENCE_SENSOR_TOPIC),
                                                new TelemetryMessage<>(genericBooleanPresenceSensorResource.getType(), updatedValue));

                                        if(parkingLed != null){
                                            if (genericBooleanPresenceSensorResource.loadUpdatedValue()) {
                                                    changeLedColor(ParkingLed.OCCUPIED_PARKING_LED_COLOR);
                                            } else {
                                                changeLedColor(ParkingLed.FREE_PARKING_LED_COLOR);
                                            }
                                        }
                                    } catch (MqttException | JsonProcessingException e) {
                                        logger.error("MQttException or JsonProcessingException {}",e.getLocalizedMessage());

                                    }
                                }
                            });


                        }
                        else if(abstractPresenceSensor.isInteger()) {
                            GenericIntPresenceSensorResource genericIntPresenceSensorResource = (GenericIntPresenceSensorResource) abstractPresenceSensor;
                            value.addDataListener(new ResourceDataListener() {
                                @Override
                                public void onDataChanged(GenericResource resource, Object updatedValue) {
                                    try {
                                        boolean carPresence= false;
                                        logger.info("Presence Sensor now is  {}", updatedValue);
                                        if(updatedValue != null)
                                        {

                                            if (updatedValue.equals(GenericIntPresenceSensorResource.TRUE_NUMBER)){
                                                carPresence = true;
                                            }
                                            else if(updatedValue.equals(GenericIntPresenceSensorResource.FALSE_NUMBER)){
                                                carPresence = false;
                                            }
                                            else{
                                                logger.error("Unexpected updatedValue");

                                            }
                                        }else{
                                            logger.error("updated Value is null");
                                        }
                                        publishTelemetryData(
                                                String.format("%s/%s/%s/%s/%s", BASIC_TOPIC, lotId, PARKING_TOPIC, parkingId, PRESENCE_SENSOR_TOPIC),
                                                new TelemetryMessage<>(genericIntPresenceSensorResource.getType(),carPresence ));

                                        if(parkingLed != null){
                                            if (carPresence) {
                                                    changeLedColor(ParkingLed.OCCUPIED_PARKING_LED_COLOR);
                                            } else {
                                                changeLedColor(ParkingLed.FREE_PARKING_LED_COLOR);
                                            }
                                        }

                                    } catch (MqttException | JsonProcessingException e) {
                                        logger.error("MQttExpeption or JsonProcessingException {}",e.getLocalizedMessage());

                                    }
                                }
                            });
                        }

                    }
                }else{
                    logger.error("NUll value or null key inside a resource mapper");
                }
            });
        }catch (Error e){
            logger.info("Error Registering resources {}",e.getLocalizedMessage());
        }

    }

    public void SubscribeToAvailabilityChannel(){
        try{

            String deviceControlTopic = String.format("%s/%s/%s/%s/%s", BASIC_TOPIC, lotId,PARKING_TOPIC,parkingId, AVAILABILITY_TOPIC);

            logger.info("Registering to Control Topic ({}) ... ", deviceControlTopic);

            this.mqttClient.subscribe(deviceControlTopic, new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage msg) throws Exception {
                    if(msg != null) {

                        try{
                            Optional <ParkingAvailabilityControlMessage> optionalParkingAvailabilityControlMessage=parseParkingAvailabilityControlMessage(msg);
                            if(optionalParkingAvailabilityControlMessage.isPresent()){
                                    AvailabilityControlDescriptor availabilityControlDescriptor = (AvailabilityControlDescriptor) optionalParkingAvailabilityControlMessage.get().getDataValue();
                                    unavailable =availabilityControlDescriptor.isUnavailable();
                                    if(unavailable){
                                        changeLedColor(ParkingLed.UNAVAILABLE_PARKING_LED_COLOR);
                                        logger.info("Parking {} is now unavailable",parkingDescriptor.getParkingId());
                                    }else {
                                        logger.info("Parking {} is now available",parkingDescriptor.getParkingId());
                                    }

                            }else {
                                logger.info("Empty or wrong ParkingAvailabilityControlMessage");
                            }
                        } catch (Exception e){
                            logger.error(e.getLocalizedMessage());
                        }
                    }
                    else {
                        logger.error("[CONTROL CHANNEL] -> Null ParkingAvailabilityControlMessage received !");
                    }
                }
            });

        }catch (Exception e){
            logger.error("ERROR Registering to Control Channel ! Msg: {}", e.getLocalizedMessage());
        }
    }

    public void ParkingRegister(){
        logger.info("Starting to Register the parking");
        try {
            publishRegisterData(
                    String.format("%s/%s/%s", BASIC_TOPIC, lotId,REGISTER_TOPIC),
                    new RegisterMessage(REGISTER_PARKING_TYPE, parkingDescriptor)
            );
        } catch (MqttException | JsonProcessingException e) {
            e.getLocalizedMessage();
            logger.error("Error Registering Parking");
        }
    }

    public void changeLedColor(String newColor){
        try{
            if(parkingLed != null){
                if(unavailable) {
                    logger.info("Not Possible to change led color , parking is not available ");
                }else{
                    parkingLed.setColor(newColor);
                    logger.info(("Correctly changed led color"));
                }
            }
        }catch (Exception e){
            logger.error(e.getLocalizedMessage());
        }


    }

    private Optional<ParkingAvailabilityControlMessage> parseParkingAvailabilityControlMessage(MqttMessage msg) {
        try {
            if (msg == null) {
                logger.error("Null message received in ParkingAvailabilityControlMessage");
                return Optional.empty();
            }
            byte[] payloadByteArray = msg.getPayload();
            String payloadString = new String(payloadByteArray);

            return Optional.ofNullable(mapper.readValue(payloadString, new TypeReference<ParkingAvailabilityControlMessage>() {
            }));

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            return Optional.empty();
        }
    }

    private void publishTelemetryData(String topic, TelemetryMessage<?> telemetryMessage) throws MqttException, JsonProcessingException {

        logger.info("Sending to topic: {} -> Data: {}", topic, telemetryMessage);

        if(this.mqttClient != null && this.mqttClient.isConnected() && telemetryMessage != null && topic != null){

            String messagePayload = mapper.writeValueAsString(telemetryMessage);

            MqttMessage mqttMessage = new MqttMessage(messagePayload.getBytes());
            mqttMessage.setQos(QOS_VALUE);

            mqttClient.publish(topic, mqttMessage);

            logger.info("Data Correctly Published to topic: {}", topic);

        }
        else
            logger.error("Error: Topic or Msg = Null or MQTT Client is not Connected !");
    }

    private void publishRegisterData(String topic, RegisterMessage registerMessage) throws MqttException, JsonProcessingException {

        logger.info("Sending to topic: {} -> Data: {}", topic, registerMessage);

        if(this.mqttClient != null && this.mqttClient.isConnected() && registerMessage != null && topic != null) {

            try {
                String messagePayload = mapper.writeValueAsString(registerMessage);
                MqttMessage mqttMessage = new MqttMessage(messagePayload.getBytes());
                mqttMessage.setQos(QOS_REGISTER_VALUE);

                mqttClient.publish(topic, mqttMessage);

                logger.info("Data Correctly Published to topic: {} ,", topic);
                logger.info("Registration of parking {} went successfully", parkingId);
            } catch (Exception e) {
                logger.error("Error publishing Register Data {}",e.getLocalizedMessage());
            }
        }
        else
            logger.error("Error: Topic or Msg = Null or MQTT Client is not Connected !");
    }

    public String getLotId() {
        return lotId;
    }

    public void setLotId(String lotId) {
        this.lotId = lotId;
    }

    public GpsDescriptor getGpsDescriptor() {
        return gpsDescriptor;
    }

    public void setGpsDescriptor(GpsDescriptor gpsDescriptor) {
        this.gpsDescriptor = gpsDescriptor;
    }

    public MqttClient getMqttClient() {
        return mqttClient;
    }

    public void setMqttClient(MqttClient mqttClient) {
        this.mqttClient = mqttClient;
    }

    public String getParkingId() {
        return parkingId;
    }

    public void setParkingId(String parkingId) {
        this.parkingId = parkingId;
    }

    public HashMap<String, GenericResource> getResourceHashMap() {
        return resourceHashMap;
    }

    public void setResourceHashMap(HashMap<String, GenericResource> resourceHashMap) {
        this.resourceHashMap = resourceHashMap;
    }

}
