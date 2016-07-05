package hr.m2stanic.smartbuilding;

/**
 * Created by mato on 22.04.16..
 */
import com.pi4j.io.gpio.*;

import java.util.HashMap;

public class ControlGpio {

    public static void roomState(Apartment apartment, HashMap<String, Pin> apartmentRooms){
        Pin pinToChange = null;
        boolean state;

        //kitchen
        pinToChange = apartmentRooms.get("kitchen");
        state = apartment.isKitchen();
        processRoom(pinToChange, state);

        //bedroom
        pinToChange = apartmentRooms.get("bedroom");
        state = apartment.isBedroom();
        processRoom(pinToChange, state);

        //bathroom
        pinToChange = apartmentRooms.get("bathroom");
        state = apartment.isBathroom();
        processRoom(pinToChange, state);

        //hallway
        pinToChange = apartmentRooms.get("hallway");
        state = apartment.isHallway();
        processRoom(pinToChange, state);

        //livingroom
        pinToChange = apartmentRooms.get("livingroom");
        state = apartment.isLivingRoom();
        processRoom(pinToChange, state);
    }

    private static void processRoom(Pin pinToChange, boolean action){
        final GpioController gpio = GpioFactory.getInstance();
        PinState wantedState = null;
        //to get room and action we need
        if (action)
            wantedState = PinState.HIGH;
        else
            wantedState = PinState.LOW;

        GpioPinDigitalOutput p = gpio.provisionDigitalOutputPin(pinToChange);
        PinState currentPinState = p.getState();
        System.out.println("MANUAL | pin: " + pinToChange.getName() + ", current state: " + currentPinState.getName() + ", wanted state: " + wantedState.getName());

        if(!currentPinState.equals(wantedState))
            p.setState(wantedState);

        gpio.shutdown();
        gpio.unprovisionPin(p);
    }
}