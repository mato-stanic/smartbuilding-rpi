package hr.m2stanic.smartbuilding;

import com.pi4j.io.gpio.Pin;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mato on 21.04.16..
 */
public class ProcessData {

    public static void processCrons(List<ApartmentCronJob> apartmentCronJobs, HashMap<Long, HashMap<String, Pin>> apartmentGpios){
        String currentDay="";
        String time = "";
        Calendar calendar = Calendar.getInstance();
        time = new SimpleDateFormat("HH:mm").format(calendar.getTime());
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.MONDAY:currentDay="monday";break;
            case Calendar.TUESDAY:currentDay="tuesday";break;
            case Calendar.WEDNESDAY:currentDay="wednesday";break;
            case Calendar.THURSDAY:currentDay="thursday";break;
            case Calendar.FRIDAY:currentDay="friday";break;
            case Calendar.SATURDAY:currentDay="saturday";break;
            case Calendar.SUNDAY:currentDay="sunday";break;
        }

        for (ApartmentCronJob apartmentCronJob : apartmentCronJobs) {
            //get rooms for apartment from hashmap

            HashMap<String, Pin> apartmentRooms = apartmentGpios.get(Long.valueOf(apartmentCronJob.getApartmentId()));
            List<String> daysList = apartmentCronJob.getDays();
            if(daysList.contains(currentDay)){
                if(apartmentCronJob.getTime().equals(time))
                {
                    Pin pinToChange = apartmentRooms.get(apartmentCronJob.getRoom());
                    ControlGpio.cronControl(apartmentCronJob.getAction(), pinToChange);
                }
            }
        }
    }

    public static void processApartmentState(List<Apartment> apartments, HashMap<Long, HashMap<String, Pin>> apartmentGpios){

        //todo: ovo tu treba ispravit jel nece radit dok ne spojim ostale led
        for (Apartment apartment : apartments) {
            if(apartment.getApartmentId() == 103){
                HashMap<String, Pin> apartmentRooms = apartmentGpios.get(apartment.getApartmentId());

                for (Map.Entry<String, Pin> stringPinEntry : apartmentRooms.entrySet()) {
                    System.out.println(stringPinEntry);
                }
                ControlGpio.roomState(apartment, apartmentRooms);
            }

        }
    }
}
