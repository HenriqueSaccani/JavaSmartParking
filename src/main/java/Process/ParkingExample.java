package Process;

import Device.ParkingSmartObject;
import Resource.ParkingLed;
import Resource.PresenceSensorResourceBash;
import Utils.GpsDescriptor;
import Utils.RandomGPSLocation;
import Configuration.SmartParkingConfiguration;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.UUID;

//related to a single parking inside a Parking Lot
//example of path : smartparking/lot/mantova001/parking/134891341a4afc/
public class ParkingExample {
    private static final Logger logger = LoggerFactory.getLogger(ParkingExample.class);
    private static final String LOT_ID = "mantova001";
    private static final GpsDescriptor gpsDescriptor = RandomGPSLocation.GetRandomGpsLocation();
    private static final String BROKER_ADDRESS = SmartParkingConfiguration.BROKER_ADDRESS; //Raspberry Pi broker
    private static final int BROKER_PORT = SmartParkingConfiguration.BROKER_PORT;

    public static void main (String [] args) {
        try{
            //Previously given parking id
            String parkingId="0001";
            //Generate Random parking UUID
            String MQTTparkingId = UUID.randomUUID().toString();

            //Create MQTT Client
            MqttClientPersistence persistence = new MemoryPersistence();
            MqttClient mqttClient = new MqttClient(String.format("tcp://%s:%d",
                    BROKER_ADDRESS,
                    BROKER_PORT),
                    MQTTparkingId,
                    persistence);

            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);

            //Connect to MQTT Broker
            mqttClient.connect(options);

            logger.info("MQTT Client Connected ! Client Id: {}", MQTTparkingId);
            //init(MqttClient mqttClient,String LOT_ID, String producer, GpsDescriptor gpsDescriptor,String parkingId,HashMap<String,GenericResource> resourceHashMap)

            ParkingSmartObject parkingSmartObject = new ParkingSmartObject() ;
            parkingSmartObject.init( LOT_ID,gpsDescriptor,mqttClient,parkingId, new HashMap<>(){
                {
                    put("led", new ParkingLed());
                    put("presence", new PresenceSensorResourceBash());
                }
            });

            parkingSmartObject.start();

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
