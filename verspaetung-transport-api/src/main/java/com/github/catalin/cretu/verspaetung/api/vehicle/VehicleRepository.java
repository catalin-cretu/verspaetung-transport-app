package com.github.catalin.cretu.verspaetung.api.vehicle;

import java.time.LocalTime;
import java.util.List;

public interface VehicleRepository {

    List<Vehicle> findAll();

    List<Vehicle> findByLineName(final String name);

    boolean stopIdExists(final Long stopId);

    List<Vehicle> findNextArrivingAtStop(final Long stopId);

    List<Vehicle> findByStop(final LocalTime stopTime, final Integer stopXCoordinate, final Integer stopYCoordinate);
}