package hr.m2stanic.smartbuilding;

import java.util.List;

/**
 * Created by mato on 21.04.16..
 */
public class ApartmentCronJob {

    private int id;
    private int apartmentId;
    private String time;
    private String action;
    private String room;
    private List<String> days;


    public ApartmentCronJob(int id, int apartmentId, String time, String action, String room) {
        this.id = id;
        this.apartmentId = apartmentId;
        this.time = time;
        this.action = action;
        this.room = room;
    }

    public ApartmentCronJob(){}

    @Override
    public String toString() {
        return "ApartmentCronJob{" +
                "id=" + id +
                ", apartmentId=" + apartmentId +
                ", time='" + time + '\'' +
                ", action='" + action + '\'' +
                ", room='" + room + '\'' +
                ", days=" + days +
                '}';
    }

    public List<String> getDays() {
        return days;
    }

    public void setDays(List<String> days) {
        this.days = days;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(int apartmentId) {
        this.apartmentId = apartmentId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
