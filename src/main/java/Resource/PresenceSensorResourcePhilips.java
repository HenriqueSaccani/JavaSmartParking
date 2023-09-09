package Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class PresenceSensorResourcePhilips extends GenericBooleanPresenceSensorResource {
    private String producer = "Philips";

    private Random random;

    public static final long UPDATE_PERIOD = 10000; //10 Seconds

    public static final long TASK_DELAY_TIME = 5000; //Seconds before starting the periodic update task

    private static final Logger logger = LoggerFactory.getLogger(PresenceSensorResourcePhilips.class);

    public PresenceSensorResourcePhilips(){
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
                    boolean aux = random.nextBoolean();
                    if (aux != carPresence){
                        carPresence=aux;
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
}
