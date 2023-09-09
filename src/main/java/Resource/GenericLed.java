package Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.UUID;

public class GenericLed extends GenericResource<String> {
    private static final Logger logger = LoggerFactory.getLogger(GenericLed.class);
    public static final String RESOURCE_TYPE ="iot:sensor:led";
    private boolean On = true;
    private ArrayList <String> possibleColors;
    private String actualColor;

    public GenericLed(){
        super(RESOURCE_TYPE,UUID.randomUUID().toString());
    }
    public GenericLed(ArrayList <String> possibleColors){
        super(RESOURCE_TYPE,UUID.randomUUID().toString());
        this.possibleColors=possibleColors;
    }

    public String getActualColor() {
        return actualColor;
    }

    public void setColor(String actualColor) {
        if (On) {
            if (possibleColors.contains(actualColor)) {
                if (!actualColor.equals(this.actualColor)) {
                    logger.info("Led color from led id {} changed to color:{}", this.getId(),actualColor);
                    this.actualColor = actualColor;
                    notifyUpdate(actualColor);
                } else {
                    logger.info("Invalid led color change, trying to change to the same color, color:{}", actualColor);
                }
            } else {
                logger.error("Trying to set invalid Color {} ", actualColor);
            }
            this.actualColor = actualColor;
        } else {
            logger.error("The led {} is Off",this.getId());
        }
    }

    public boolean isOn() {
        return On;
    }

    public void setOn(boolean on) {
        On = on;
    }

    public ArrayList<String> getPossibleColors() {
        return possibleColors;
    }

    public void setPossibleColors(ArrayList<String> possibleColors) {
        this.possibleColors = possibleColors;
    }



    @Override
    public String loadUpdatedValue() {
        try{
            if (On=true){
                return actualColor;
            }
        }catch (Error e){
            logger.error("Error in the led, msg {}",e.getLocalizedMessage());
        }
        return null;
    }
}
