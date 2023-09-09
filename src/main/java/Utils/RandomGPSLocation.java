package Utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class RandomGPSLocation {
    private static final Logger logger = LoggerFactory.getLogger(RandomGPSLocation.class);
    private static final int MAX_LONGITUDE = 180;
    private static final int MAX_LATITUDE = 180;
    private static final int MAX_high = 500; //m

    public static GpsDescriptor GetRandomGpsLocation(){
        Random random=new Random(System.currentTimeMillis());
        float latitude=random.nextInt()*MAX_LATITUDE;
        float longitude=random.nextInt()*MAX_LONGITUDE;
        float high=random.nextInt()*MAX_high;

        boolean negative= random.nextBoolean();
        if (negative)
            longitude*=-1;

        negative= random.nextBoolean();

        if (negative)
                latitude*=-1;

        negative= random.nextBoolean();

        if (negative)
            high*=-1;

        return new GpsDescriptor(latitude,longitude,high);

    }
    //Always high equal 0
    public static GpsDescriptor GetRandomLocationInSeaLevel(){
        Random random=new Random(System.currentTimeMillis());
        float latitude=random.nextInt()*MAX_LATITUDE;
        float longitude=random.nextInt()*MAX_LONGITUDE;
        boolean negative = random.nextBoolean();
        if (negative) {
            longitude*=-1;
        }

        negative= random.nextBoolean();

        if (negative) {
            latitude*=-1;
        }

        return new GpsDescriptor(latitude,longitude);
    }

    public static void main (String[] args) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        GpsDescriptor gpsDescriptor = RandomGPSLocation.GetRandomLocationInSeaLevel() ;
        logger.info("{}", objectMapper.writeValueAsString(gpsDescriptor));
        logger.info("{}", objectMapper.writeValueAsString(GetRandomGpsLocation()));
    }
}
