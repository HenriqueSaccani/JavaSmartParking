package Process;

import Device.DataCollectorAndManager;
import Configuration.SmartParkingConfiguration;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class DataCollectorAndManagerExample {
    private static final Logger logger = LoggerFactory.getLogger(DataCollectorAndManagerExample.class);
    private static final String LOT_ID = "mantova001";
    private static final String BROKER_ADDRESS = SmartParkingConfiguration.BROKER_ADDRESS; //Raspberry Pi broker
    private static final int BROKER_PORT = SmartParkingConfiguration.BROKER_PORT;

    public static void main (String [] args) {
        try{
            String MQTTParkingLotId = UUID.randomUUID().toString();

            //Create MQTT Client
            MqttClientPersistence persistence = new MemoryPersistence();
            MqttClient mqttClient = new MqttClient(String.format("tcp://%s:%d",
                    BROKER_ADDRESS,
                    BROKER_PORT),
                    MQTTParkingLotId,
                    persistence);

            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);

            //Connect to MQTT Broker
            mqttClient.connect(options);

            logger.info("MQTT Client Connected ! Client Id: {}", MQTTParkingLotId);
            //init(MqttClient mqttClient,String LOT_ID, String producer, GpsDescriptor gpsDescriptor,String parkingId,HashMap<String,GenericResource> resourceHashMap)

            DataCollectorAndManager dataCollectorAndManager = new DataCollectorAndManager() ;
            dataCollectorAndManager.init(mqttClient, LOT_ID);
            dataCollectorAndManager.start();

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
