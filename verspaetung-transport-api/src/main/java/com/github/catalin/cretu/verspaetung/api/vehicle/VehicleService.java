package com.github.catalin.cretu.verspaetung.api.vehicle;

import com.github.catalin.cretu.verspaetung.Result;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;
import java.util.List;

@Slf4j
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(final VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public List<Vehicle> findAllVehicles() {
        log.debug("Find all vehicles");

        return vehicleRepository.findAll();
    }

    public Result<List<Vehicle>> findByLineName(final String name) {
        if (name.isBlank()) {
            return Result.error("lineName", "Line name must not be blank");
        }
        log.info("Find vehicles with line with name [{}]", name);

        var vehicles = vehicleRepository.findByLineName(name);
        return Result.ok(vehicles);
    }

    public Result<List<Vehicle>> findNextAtStop(final Long stopId) {
        if (!vehicleRepository.stopIdExists(stopId)) {
            return Result.error("stopId", "Cannot find stop with ID: " + stopId);
        }
        log.info("Find vehicles arriving next at stop with id [{}]", stopId);

        var vehicles = vehicleRepository.findNextArrivingAtStop(stopId);
        return Result.ok(vehicles);
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