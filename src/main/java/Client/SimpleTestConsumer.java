package Client;

import Process.ParkingExample;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class SimpleTestConsumer {

    private static final Logger logger = LoggerFactory.getLogger(ParkingExample.class);


    //IP Address of the target MQTT Broker
    private static String BROKER_ADDRESS = "192.168.1.114";

    //PORT of the target MQTT Broker
    private static int BROKER_PORT = 1883;

    private static final String TARGET_TOPIC = "#";

    public static void main(String [] args) {

        logger.info("MQTT Consumer Tester Started ...");

        try{

            //Generate a random MQTT client ID using the UUID class
            String clientId = UUID.randomUUID().toString();
            clientId = "df2cf55c-19ff-4f94-8a50-0bc41eef4bb6";
            //Represents a persistent data store, used to store outbound and inbound messages while they
            //are in flight, enabling delivery to the QoS specified. In that case use a memory persistence.
            //When the application stops all the temporary data will be deleted.
            MqttClientPersistence persistence = new MemoryPersistence();

            //The persistence is not passed to the constructor the default file persistence is used.
            //In case of a file-based storage the same MQTT client UUID should be used
            IMqttClient client = new MqttClient(
                    String.format("tcp://%s:%d", BROKER_ADDRESS, BROKER_PORT), //Create the URL from IP and PORT
                    clientId,
                    persistence);

            //Define MQTT Connection Options such as reconnection, persistent/clean session and connection timeout
            //Authentication option can be added -> See AuthProducer example
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(false);
            options.setConnectionTimeout(10);

            //Connect to the target broker
            client.connect(options);

            logger.info("Connected ! Client Id: {}", clientId);

            //Subscribe to the target topic #. In that case the consumer will receive (if authorized) all the message
            //passing through the broker
            client.subscribe(TARGET_TOPIC, (topic, msg) -> {
                //The topic variable contain the specific topic associated to the received message. Using MQTT wildcards
                //messaged from multiple and different topic can be received with the same subscription
                //The msg variable is a MqttMessage object containing all the information about the received message
                byte[] payload = msg.getPayload();
                logger.info("Message Received -> Topic: {} - Payload: {}", topic, new String(payload));
            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
