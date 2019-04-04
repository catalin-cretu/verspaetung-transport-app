package com.github.catalin.cretu.verspaetung.api.vehicle;

import java.util.List;

public interface VehicleRepository {

    List<Vehicle> findAll();

    List<Vehicle> findByLineName(final String name);

    boolean stopIdExists(final Long stopId);

    List<Vehicle> findNextArrivingAtStop(final Long stopId);
}