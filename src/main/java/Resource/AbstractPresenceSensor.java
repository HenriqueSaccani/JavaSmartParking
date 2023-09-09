package Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public abstract class AbstractPresenceSensor<T> extends GenericResource<T> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractPresenceSensor.class);

    public abstract Boolean isBoolean();

    public static final String RESOURCE_TYPE ="iot:sensor:presence";

    public abstract Boolean isInteger();


    public AbstractPresenceSensor(){
        super(AbstractPresenceSensor.RESOURCE_TYPE,UUID.randomUUID().toString());
        init();
    }
    private void init(){

        try{
            startPeriodicEventValueUpdateTask();

        }catch (Exception e){
            logger.error("Error init Battery Resource Object ! Msg: {}", e.getLocalizedMessage());
        }

    }

    abstract void startPeriodicEventValueUpdateTask();
}
