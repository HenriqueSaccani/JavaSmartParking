package Process;

import Configuration.SmartParkingConfiguration;
import Device.ExternalServer;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class SimpleExternalServer {
    private static final Logger logger = LoggerFactory.getLogger(SimpleExternalServer.class);

    private static final String BROKER_ADDRESS = SmartParkingConfiguration.BROKER_ADDRESS; //Raspberry Pi broker
    private static final int BROKER_PORT = SmartParkingConfiguration.BROKER_PORT;

    public static void main (String [] args) {
        try{
            String lotId= "mantova001";
            String parkingId="0001";
            //Generate Random parking UUID
            String MQTTparkingLotId = UUID.randomUUID().toString();

            //Create MQTT Client
            MqttClientPersistence persistence = new MemoryPersistence();
            MqttClient mqttClient = new MqttClient(String.format("tcp://%s:%d",
                    BROKER_ADDRESS,
                    BROKER_PORT),
                    MQTTparkingLotId,
                    persistence);

            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);

            //Connect to MQTT Broker
            mqttClient.connect(options);

            logger.info("MQTT Client Connected ! Client Id: {}", MQTTparkingLotId);
            //init(MqttClient mqttClient,String LOT_ID, String producer, GpsDescriptor gpsDescriptor,String parkingId,HashMap<String,GenericResource> resourceHashMap)
            ExternalServer externalServer = new ExternalServer(mqttClient);
            externalServer.controlParkingUnavailability(lotId,parkingId,true);
            //externalServer.controlParkingUnavailability(lotId,parkingId,false);

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
