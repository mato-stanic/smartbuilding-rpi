package hr.m2stanic.smartbuilding;

import com.pi4j.io.gpio.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

/**
 * Created by mato on 21.04.16..
 */
public class MakeConnection {


    public static void getApartments(HashMap<Long, HashMap<String, Pin>> apartmentGpios){
        List<Apartment> apartments = new ArrayList<>();
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://127.0.0.1:9995/smartbuilding", "m2stanic", "m2stanic");
            c.setAutoCommit(false);

            stmt = c.createStatement();

            String select = " select * from apartment_layout;";
            ResultSet rs = stmt.executeQuery(select);
            while (rs.next()) {
                Long id = rs.getLong("id");
                Long apartmentId = rs.getLong("apartment_id");
                Boolean livingRoom = rs.getBoolean("living_room");
                Boolean hallway = rs.getBoolean("hallway");
                Boolean kitchen = rs.getBoolean("kitchen");
                Boolean bathroom = rs.getBoolean("bathroom");
                Boolean bedroom = rs.getBoolean("bedroom");
                Boolean motionDetection = rs.getBoolean("motion_detection"); // sensor trigger enabled
                Boolean motionDetectionEnabled = rs.getBoolean("motion_detection_enabled"); //feature eneabled or disabled

                Apartment apt = new Apartment();
                apt.setId(id);
                apt.setApartmentId(apartmentId);
                apt.setLivingRoom(livingRoom);
                apt.setHallway(hallway);
                apt.setKitchen(kitchen);
                apt.setBathroom(bathroom);
                apt.setBedroom(bedroom);
                apt.setMotionDetectionEnabled(motionDetectionEnabled);
                apt.setMotionDetection(motionDetection);
                apartments.add(apt);
            }
            rs.close();
            stmt.close();
            c.close();

            ProcessData.processApartmentState(apartments, apartmentGpios);

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }

    }

    public static void updateApartmentsStatesMotionDetection(Integer roomsToChange, Boolean state){

        List<Apartment> apartments = new ArrayList<>();
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://127.0.0.1:9995/smartbuilding", "m2stanic", "m2stanic");
            c.setAutoCommit(true);

            stmt = c.createStatement();
            String statement = null;

            if(roomsToChange == 2){
                statement = "update apartment_layout set bedroom=" + state + ", kitchen=" + state + " where apartment_id=131;";
            }
            else{
                statement = "update apartment_layout set living_room=" + state + ", bathroom=" + state + ", hallway=" + state + " where apartment_id=131;";
            }

            System.out.println("update query: " + statement);
            stmt.execute(statement);
            stmt.close();
            c.close();


        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }

    }


}
