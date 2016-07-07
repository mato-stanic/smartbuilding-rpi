package hr.m2stanic.smartbuilding;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainClass {

    public static void main(String[] argv) throws InterruptedException{

        System.out.println("Init hash map apartmentGpios");
        HashMap<Long, HashMap<String, Pin>> apartmentGpios = createHashMaps();

        System.out.println("Set all pins init to LOW");
        initPinStates(apartmentGpios);


        Runnable apartments = new Runnable() {
            @Override
            public void run() {
                MakeConnection.getApartments(apartmentGpios);
            }
        };

        ScheduledExecutorService executorApartment = Executors.newScheduledThreadPool(1);
        executorApartment.scheduleAtFixedRate(apartments, 0, 1, TimeUnit.SECONDS);


    }

    private static HashMap<Long, HashMap<String, Pin>> createHashMaps(){
        HashMap<Long, HashMap<String, Pin>> mapToReturn = new HashMap<>();

        //apartment 1a rooms combinations gpio pins
        HashMap<String, Pin> apartment1a = new HashMap<>();
        apartment1a.put("kitchen", RaspiPin.GPIO_28);
        apartment1a.put("bedroom", RaspiPin.GPIO_29);
        apartment1a.put("bathroom", RaspiPin.GPIO_27);
        apartment1a.put("hallway", RaspiPin.GPIO_25);
        apartment1a.put("livingroom", RaspiPin.GPIO_23);
        mapToReturn.put(Long.valueOf(103), apartment1a);

        //apartment 1b rooms combinations gpio pins
        HashMap<String, Pin> apartment1b = new HashMap<>();
        apartment1b.put("kitchen", RaspiPin.GPIO_10);
        apartment1b.put("bedroom", RaspiPin.GPIO_11);
        apartment1b.put("bathroom", RaspiPin.GPIO_14);
        apartment1b.put("hallway", RaspiPin.GPIO_06);
        apartment1b.put("livingroom", RaspiPin.GPIO_13);
        mapToReturn.put(Long.valueOf(104), apartment1b);

        //apartment 2a rooms combinations gpio pins
        HashMap<String, Pin> apartment2a = new HashMap<>();
        apartment2a.put("kitchen", RaspiPin.GPIO_05);
        apartment2a.put("bedroom", RaspiPin.GPIO_00);
        apartment2a.put("bathroom", RaspiPin.GPIO_02);
        apartment2a.put("hallway", RaspiPin.GPIO_03);
        apartment2a.put("livingroom", RaspiPin.GPIO_04);
        mapToReturn.put(Long.valueOf(131), apartment2a);

        return mapToReturn;
    }

    private static void initPinStates(HashMap<Long, HashMap<String, Pin>> apartmentGpios){
        final GpioController gpio = GpioFactory.getInstance();
        for (HashMap<String, Pin> stringPinHashMap : apartmentGpios.values()) {
            for (Pin pin : stringPinHashMap.values()) {
                final GpioPinDigitalOutput p = gpio.provisionDigitalOutputPin(pin, "init", PinState.LOW);
                gpio.unprovisionPin(p);
            }
        }
//        gpio.shutdown();
    }


}