package hr.m2stanic.smartbuilding;

import com.pi4j.io.gpio.Pin;

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

                Apartment apt = new Apartment();
                apt.setId(id);
                apt.setApartmentId(apartmentId);
                apt.setLivingRoom(livingRoom);
                apt.setHallway(hallway);
                apt.setKitchen(kitchen);
                apt.setBathroom(bathroom);
                apt.setBedroom(bedroom);
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


}
