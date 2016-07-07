package hr.m2stanic.smartbuilding;

/**
 * Created by mato on 22.04.16..
 */
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ControlGpio {

    static final LocalTime[] sensorBackHighTriggerTime = new LocalTime[1];
    static final LocalTime[] sensorFrontHighTriggerTime = new LocalTime[1];
    static boolean firstTrigger = true;
    static List<Pin> frontSensorRooms = new ArrayList<>(Arrays.asList(RaspiPin.GPIO_04,RaspiPin.GPIO_02,RaspiPin.GPIO_03));
    static List<Pin> backSensorRooms = new ArrayList<>(Arrays.asList(RaspiPin.GPIO_00,RaspiPin.GPIO_05));

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

        LocalTime currentTime = LocalTime.now();
        if(sensorBackHighTriggerTime[0] != null){
            long minutes = ChronoUnit.MINUTES.between(sensorBackHighTriggerTime[0], currentTime);
            long second = ChronoUnit.SECONDS.between(sensorBackHighTriggerTime[0], currentTime);
            System.out.println("Difference back: " + currentTime + " - " + sensorBackHighTriggerTime[0] + " = " + second + " s");
            if(second > 15){
                System.out.println("No back motion detected for: " + second + "s");
                sensorBackHighTriggerTime[0] = null;
                MakeConnection.updateApartmentsStatesMotionDetection(2, false);
            }
        }

        if(sensorFrontHighTriggerTime[0] != null){
            long minutes = ChronoUnit.MINUTES.between(sensorFrontHighTriggerTime[0], currentTime);
            long second = ChronoUnit.SECONDS.between(sensorFrontHighTriggerTime[0], currentTime);
            System.out.println("Difference front: " + currentTime + " - " + sensorFrontHighTriggerTime[0] + " = " + second + " s");
            if(second > 15){
                System.out.println("No front motion detected for: " + second + "s");
                sensorFrontHighTriggerTime[0] = null;
                MakeConnection.updateApartmentsStatesMotionDetection(3, false);

            }
        }

        if(apartment.isMotionDetectionEnabled()){//feature enabled
            if (apartment.isMotionDetection()){//trigger motion detection
                if(firstTrigger){
                    System.out.println("****************************************");
                    System.out.println("first trigger");
                    System.out.println("****************************************");
                    sensorBackHighTriggerTime[0] = LocalTime.now();
                    sensorFrontHighTriggerTime[0] = LocalTime.now();
                    initListener();
                    firstTrigger=false;
                }
            }
            else{
                final GpioController gpio = GpioFactory.getInstance();
                gpio.removeAllListeners();
                sensorBackHighTriggerTime[0] = null;
                sensorFrontHighTriggerTime[0] = null;
                firstTrigger=true;
                System.out.println("****************************************");
                System.out.println("motion detection disabled");
                System.out.println("****************************************");
            }
        }
    }


    static GpioPinDigitalInput sensorBack;
    static GpioPinDigitalInput sensorFront;
    private static void initListener(){
        final GpioController gpio = GpioFactory.getInstance();
        if(sensorBack == null)
            sensorBack = gpio.provisionDigitalInputPin(RaspiPin.GPIO_01, PinPullResistance.PULL_DOWN);
        if(sensorFront == null)
            sensorFront = gpio.provisionDigitalInputPin(RaspiPin.GPIO_07, PinPullResistance.PULL_DOWN);


        sensorBack.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {

                PinState sensorState = event.getState();
                System.out.println("sensor back state: " + sensorState);
                if(sensorState.equals(PinState.HIGH)){
                    MakeConnection.updateApartmentsStatesMotionDetection(2, true);
                    sensorBackHighTriggerTime[0] = LocalTime.now();
                    System.out.println("motion back detected: " + sensorBackHighTriggerTime[0]);
                }
                LocalTime currentTime = LocalTime.now();
                System.out.println("Current back trigger time: " + currentTime);

            }
        });


        sensorFront.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {

                PinState sensorState = event.getState();
                System.out.println("sensor front state: " + sensorState);
                if(sensorState.equals(PinState.HIGH)){
                    MakeConnection.updateApartmentsStatesMotionDetection(3, true);
                    sensorFrontHighTriggerTime[0] = LocalTime.now();
                    System.out.println("motion front detected: " + sensorFrontHighTriggerTime[0]);
                }

                LocalTime currentTime = LocalTime.now();
                System.out.println("Current front trigger time: " + currentTime);
            }
        });


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
//        System.out.println("MANUAL | pin: " + pinToChange.getName() + ", current state: " + currentPinState.getName() + ", wanted state: " + wantedState.getName());

        if(!currentPinState.equals(wantedState))
            p.setState(wantedState);

//        gpio.shutdown();
        gpio.unprovisionPin(p);
    }
}