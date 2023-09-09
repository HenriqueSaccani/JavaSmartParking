package Resource;

import Utils.ResourceDataListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;

//red,green and yellow allowed colors

public class ParkingLed extends GenericLed{
    private static final Logger logger = LoggerFactory.getLogger(ParkingLed.class);
    public static final String RESOURCE_TYPE ="iot:sensor:led";
    private static final ArrayList<String> POSSIBLE_COLORS=new ArrayList<>(Arrays.asList("red","green","yellow"));
    public static final String UNAVAILABLE_PARKING_LED_COLOR ="yellow";
    public static final String FREE_PARKING_LED_COLOR = "green";
    public static final String OCCUPIED_PARKING_LED_COLOR = "red";

    public ParkingLed(){
        super(POSSIBLE_COLORS);
        this.setColor(OCCUPIED_PARKING_LED_COLOR);
    }

}