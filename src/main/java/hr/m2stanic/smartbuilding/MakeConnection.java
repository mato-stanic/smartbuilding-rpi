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



        public static void getCrons(HashMap<Long, HashMap<String, Pin>> apartmentGpios){
            Map<Integer, List<String>> cronDays = new HashMap<>();
            Map<Integer, ApartmentCronJob> apartmentCronJobs = new TreeMap<>();
            Connection c = null;
            Statement stmt = null;
            try {
                Class.forName("org.postgresql.Driver");
                c = DriverManager
                        .getConnection("jdbc:postgresql://127.0.0.1:9995/smartbuilding", "m2stanic", "m2stanic");
                c.setAutoCommit(false);

                stmt = c.createStatement();

                String select = " select ac.id, ac.apartment_id, ac.time, ac.action, ac.room, acd.day " +
                        " from apartment_cron ac left outer join apartment_cron_days acd on ac.id=acd.apartment_cron_id " +
                        " order by ac.id;";
                ResultSet rs = stmt.executeQuery(select);
                while (rs.next()) {
                    Integer apartmentCronId = rs.getInt("id");
                    ApartmentCronJob acj = apartmentCronJobs.get(apartmentCronId);
                    if (acj == null) {
                        int aptId = rs.getInt("apartment_id");
                        String time = rs.getString("time");
                        String room = rs.getString("room");
                        String action = rs.getString("action");
                        acj = new ApartmentCronJob();
                        acj.setId(apartmentCronId);
                        acj.setApartmentId(aptId);
                        acj.setTime(time);
                        acj.setRoom(room);
                        acj.setAction(action);
                        apartmentCronJobs.put(apartmentCronId, acj);
                        // create a new list for this apartmentCronJob
                        cronDays.put(apartmentCronId, new ArrayList<String>());
                    }
                    if (!rs.wasNull()) {
                        List<String> days = cronDays.get(apartmentCronId);
                        days.add(rs.getString("day"));
                    }
                }
                rs.close();
                stmt.close();
                c.close();

                List<ApartmentCronJob> result = new ArrayList<>();
                for (ApartmentCronJob acj : apartmentCronJobs.values()) {
                    acj.setDays(cronDays.get(acj.getId()));
                    result.add(acj);
                }

                ProcessData.processCrons(result, apartmentGpios);

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }

    }

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
