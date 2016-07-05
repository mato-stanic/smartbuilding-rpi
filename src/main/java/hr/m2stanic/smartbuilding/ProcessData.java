package hr.m2stanic.smartbuilding;

import com.pi4j.io.gpio.Pin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mato on 21.04.16..
 */
public class ProcessData {

    public static void processApartmentState(List<Apartment> apartments, HashMap<Long, HashMap<String, Pin>> apartmentGpios){

        //todo: ovo tu treba ispravit jel nece radit dok ne spojim ostale led
        for (Apartment apartment : apartments) {
            if(apartment.getApartmentId() == 103){
                HashMap<String, Pin> apartmentRooms = apartmentGpios.get(apartment.getApartmentId());

                for (Map.Entry<String, Pin> stringPinEntry : apartmentRooms.entrySet()) {
                    System.out.println(stringPinEntry);
                }
                ControlGpio.roomState(apartment, apartmentRooms);
            }else if(apartment.getApartmentId() == 104){
                HashMap<String, Pin> apartmentRooms = apartmentGpios.get(apartment.getApartmentId());

                for (Map.Entry<String, Pin> stringPinEntry : apartmentRooms.entrySet()) {
                    System.out.println(stringPinEntry);
                }
                ControlGpio.roomState(apartment, apartmentRooms);
            }else if(apartment.getApartmentId() == 131){
                HashMap<String, Pin> apartmentRooms = apartmentGpios.get(apartment.getApartmentId());

                for (Map.Entry<String, Pin> stringPinEntry : apartmentRooms.entrySet()) {
                    System.out.println(stringPinEntry);
                }
                ControlGpio.roomState(apartment, apartmentRooms);
            }

        }
    }
}
