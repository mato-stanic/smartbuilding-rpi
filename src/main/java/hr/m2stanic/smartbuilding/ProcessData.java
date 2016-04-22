package hr.m2stanic.smartbuilding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by mato on 21.04.16..
 */
public class ProcessData {

    public static void processCrons(List<ApartmentCronJob> apartmentCronJobs){
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
            List<String> daysList = apartmentCronJob.getDays();
            if(daysList.contains(currentDay)){
                if(apartmentCronJob.getTime().equals(time))
                {
                    ControllGpio.cronControl(apartmentCronJob.getRoom(), apartmentCronJob.getAction());
                }
                    System.out.println(apartmentCronJob.toString());
            }
        }
    }
}
