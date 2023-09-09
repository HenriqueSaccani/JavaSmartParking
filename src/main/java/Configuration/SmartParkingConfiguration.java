package Configuration;

public class SmartParkingConfiguration {
    public  static final String BASIC_TOPIC ="smartparking/lot";
    public static final String PARKING_TOPIC="parking";
    public static final String PRESENCE_SENSOR_TOPIC="sensor/presence";//smartparking/lot/mantova001/parking/0001/sensor/presence
    public static final String LED_COLOR_TOPIC ="led/color";//smartparking/lot/mantova001/parking/0001/led/color
    public static final String REGISTER_TOPIC="parkingregister";//smartparking/lot/mantova001/parkingregister
    public static final String AVAILABILITY_TOPIC ="availability";//smartparking/lot/mantova001/parking/0001/availability
    public static final String INFORMATION_TOPIC="information";//smartparking/lot/mantova001/parking/information
    public static final String CONTROL_AVAILABILITY_TOPIC="control";//smartparking/lot/mantova001/control

    public static final String MESSAGE_PARKING_LOT_INFORMATION_TYPE = "parking_information_message";
    public static final String MESSAGE_REGISTER_PARKING_TYPE = "parking_register_message";
    public static final String MESSAGE_PARKING_AVAILABILITY_CONTROL_TYPE = "parking_availability_message";
    public static final String MESSAGE_PARKING_RESERVE_TYPE = "parking_reserve_message";

    public static final int QOS_VALUE=1;
    public static final int QOS_REGISTER_VALUE = 1;

    public static final String BROKER_ADDRESS ="192.168.1.114"; //Raspberry Pi broker
    public static final int BROKER_PORT = 1883;
}
