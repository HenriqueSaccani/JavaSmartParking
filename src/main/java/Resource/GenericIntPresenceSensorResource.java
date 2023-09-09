package Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
//Generic Presence sensor that returns int values. carPresence can ONLY be 1 or 0
public abstract class GenericIntPresenceSensorResource extends AbstractPresenceSensor<Integer> {
    public static final Integer TRUE_NUMBER = 1;

    public  static final Integer FALSE_NUMBER = 0;

    protected Integer carPresence;

    public GenericIntPresenceSensorResource(){
        super();
    }

    @Override
    public Integer loadUpdatedValue() {
        return carPresence;
    }


    @Override
    public String toString() {
        return "PresenceSensorResource{" +
                ", CarPresence=" + carPresence +
                '}';
    }

    @Override
    public Boolean isBoolean() {
        return false;
    }

    @Override
    public Boolean isInteger() {
        return true;
    }
}
