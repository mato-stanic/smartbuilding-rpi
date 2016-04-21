package hr.m2stanic.smartbuilding;

/**
 * Created by mato on 21.04.16..
 */
public class Apartment {

    private Long id;
    private Long apartmentId;
    private boolean livingRoom;
    private boolean hallway;
    private boolean kitchen;
    private boolean bathroom;
    private boolean bedroom;

    public Apartment(Long id, Long apartmentId, boolean livingRoom, boolean hallway, boolean kitchen, boolean bathroom, boolean bedroom) {
        this.id = id;
        this.apartmentId = apartmentId;
        this.livingRoom = livingRoom;
        this.hallway = hallway;
        this.kitchen = kitchen;
        this.bathroom = bathroom;
        this.bedroom = bedroom;
    }

    public Apartment(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(Long apartmentId) {
        this.apartmentId = apartmentId;
    }

    public boolean isLivingRoom() {
        return livingRoom;
    }

    public void setLivingRoom(boolean livingRoom) {
        this.livingRoom = livingRoom;
    }

    public boolean isHallway() {
        return hallway;
    }

    public void setHallway(boolean hallway) {
        this.hallway = hallway;
    }

    public boolean isKitchen() {
        return kitchen;
    }

    public void setKitchen(boolean kitchen) {
        this.kitchen = kitchen;
    }

    public boolean isBathroom() {
        return bathroom;
    }

    public void setBathroom(boolean bathroom) {
        this.bathroom = bathroom;
    }

    public boolean isBedroom() {
        return bedroom;
    }

    public void setBedroom(boolean bedroom) {
        this.bedroom = bedroom;
    }

    @Override
    public String toString() {
        return "Apartment{" +
                "id=" + id +
                ", apartmentId=" + apartmentId +
                ", livingRoom=" + livingRoom +
                ", hallway=" + hallway +
                ", kitchen=" + kitchen +
                ", bathroom=" + bathroom +
                ", bedroom=" + bedroom +
                '}';
    }
}
