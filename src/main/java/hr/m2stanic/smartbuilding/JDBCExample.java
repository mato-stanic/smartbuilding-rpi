package hr.m2stanic.smartbuilding;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class JDBCExample {

    public static void main(String[] argv) {


        Runnable apartmentCrons = new Runnable() {
            public void run() {
                MakeConnection.getCrons();
            }
        };

        Runnable apartments = new Runnable() {
            @Override
            public void run() {
                MakeConnection.getApartments();
            }
        };

        ScheduledExecutorService executorCrons = Executors.newScheduledThreadPool(1);
        executorCrons.scheduleAtFixedRate(apartmentCrons, 0, 30, TimeUnit.SECONDS);

        ScheduledExecutorService executorApartment = Executors.newScheduledThreadPool(1);
        executorApartment.scheduleAtFixedRate(apartments, 0, 5, TimeUnit.SECONDS);

    }



}