package com.github.catalin.cretu.verspaetung.api.vehicle;

import com.github.catalin.cretu.verspaetung.Result;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

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

        var optionalVehicle = vehicleRepository.findByLineName(name);
        return optionalVehicle
                .map(vehicle -> Result.ok(List.of(vehicle)))
                .orElse(Result.ok(List.of()));
    }

    public Result<List<Vehicle>> findNextAtStop(final Long stopId) {
        log.info("Find vehicles arriving next at stop with id [{}]", stopId);

        List<Vehicle> vehicles = vehicleRepository.findAll();
        var vehiclesStopping = vehicles.stream()
                .filter(vehicle -> hasStop(stopId, vehicle))
                .collect(toList());

        if (vehiclesStopping.isEmpty()) {
            return Result.ok(List.of());
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
        var nextArrivingVehicle =
                vehicleByDelayedTime.keySet().stream()
                        .min(LocalTime::compareTo)
                        .map(vehicleByDelayedTime::get)
                        .orElseThrow();
        return Result.ok(List.of(nextArrivingVehicle));
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