package Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//boolean type
public abstract class GenericBooleanPresenceSensorResource extends AbstractPresenceSensor<Boolean> {

    private static final Logger logger = LoggerFactory.getLogger(GenericBooleanPresenceSensorResource.class);

    protected Boolean carPresence=false;

    public GenericBooleanPresenceSensorResource(){
        super();
    }

    @Override
    public Boolean loadUpdatedValue() {
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
        return true;
    }

    @Override
    public Boolean isInteger() {
        return false;
    }

}

