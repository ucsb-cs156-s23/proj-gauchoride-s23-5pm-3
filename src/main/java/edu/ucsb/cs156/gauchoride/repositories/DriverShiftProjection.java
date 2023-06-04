package edu.ucsb.cs156.gauchoride.repositories;

public interface DriverShiftProjection {
    long getId();
    String getDay();
    String getStartTime();
    String getStopTime();
    UserProjection getDriver();

    interface UserProjection {
        long getId();
        String getFullName();
    }
} 
