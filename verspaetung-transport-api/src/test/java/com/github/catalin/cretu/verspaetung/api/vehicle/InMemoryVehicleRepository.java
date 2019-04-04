package com.github.catalin.cretu.verspaetung.api.vehicle;

import java.time.LocalTime;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class InMemoryVehicleRepository implements VehicleRepository {

    private final List<Long> stopIds;
    private final List<Vehicle> vehicles;
    private long id = 0;

    public InMemoryVehicleRepository(final List<Long> stopIds, final List<Vehicle> vehicles) {
        this.stopIds = stopIds;
        this.vehicles = vehicles;
    }

    @Override
    public List<Vehicle> findAll() {
        return vehicles;
    }

    @Override
    public List<Vehicle> findByLineName(final String name) {
        return vehicles.stream()
                .filter(vehicle -> name.equals(vehicle.getLine().getName()))
                .collect(toList());
    }

    @Override
    public boolean stopIdExists(final Long stopId) {
        var optionalFoundId = stopIds.stream()
                .filter(stopId::equals)
                .findFirst();
        if (optionalFoundId.isPresent()) {
            return true;
        }
        return vehicles.stream()
                .flatMap(vehicle -> vehicle.getLine().getStops().stream())
                .anyMatch(line -> stopId.equals(line.getId()));
    }

    @Override
    public List<Vehicle> findNextArrivingAtStop(final Long stopId) {
        var vehiclesStopping = vehicles.stream()
                .filter(vehicle -> hasStop(stopId, vehicle))
                .collect(toList());
        if (vehiclesStopping.isEmpty()) {
            return emptyList();
        }
        var normalStopTime = vehiclesStopping.get(0).getLine().getStops()
                .stream()
                .filter(stop -> stop.getId().equals(stopId))
                .map(Stop::getTime)
                .findFirst()
                .orElseThrow();

        var vehicleByDelayedTime = vehiclesStopping.stream()
                .collect(toMap(
                        vehicle -> toDelayedLocalTime(normalStopTime, vehicle),
                        vehicle -> vehicle));
        return vehicleByDelayedTime.keySet().stream()
                .sorted(LocalTime::compareTo)
                .map(vehicleByDelayedTime::get)
                .collect(toList());
    }

    private static LocalTime toDelayedLocalTime(final LocalTime normalStopTime, final Vehicle vehicle) {
        return normalStopTime.plusMinutes(vehicle.getLine().getDelay());
    }

    private static boolean hasStop(final Long stopId, final Vehicle vehicle) {
        return vehicle.getLine().getStops()
                .stream()
                .anyMatch(stop -> stop.getId().equals(stopId));
    }
}