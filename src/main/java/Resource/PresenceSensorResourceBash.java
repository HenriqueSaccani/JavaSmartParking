package Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class PresenceSensorResourceBash extends GenericIntPresenceSensorResource {

    private String producer = "Bash";

    private Random random;

    public static final long UPDATE_PERIOD = 7000; //7 Seconds

    public static final long TASK_DELAY_TIME = 10000; //Seconds before starting the periodic update task

    private static final Logger logger = LoggerFactory.getLogger(PresenceSensorResourceBash.class);

    private Integer carPresence=0; // can be just 0 or 1

    public PresenceSensorResourceBash (){
        super();
    }

    @Override
    void startPeriodicEventValueUpdateTask() {
        try{
            this.random = new Random(System.currentTimeMillis());
            logger.info("Starting periodic Update Task presence sensor with period: {} ms", UPDATE_PERIOD);

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    logger.info("Changing PresenceSensor status");
                    boolean aux = random.nextBoolean(); //if true change value of CarPresence
                    if (aux){
                        if (carPresence == 0){
                            carPresence = 1;
                        }
                        else {
                            carPresence = 0;
                        }
                        notifyUpdate(carPresence);
                    }

                }
            }, TASK_DELAY_TIME, UPDATE_PERIOD);

        }catch (Exception e){
            logger.error("Error executing periodic resource value ! Msg: {}", e.getLocalizedMessage());
        }
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public Integer getCarPresence() {
        return carPresence;
    }
}
