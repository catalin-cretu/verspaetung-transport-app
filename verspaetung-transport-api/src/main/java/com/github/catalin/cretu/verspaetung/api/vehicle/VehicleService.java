package com.github.catalin.cretu.verspaetung.api.vehicle;

import com.github.catalin.cretu.verspaetung.ErrorResult;
import com.github.catalin.cretu.verspaetung.Result;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.github.catalin.cretu.verspaetung.ErrorResult.errorResult;

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
        log.info("Find vehicles arriving at line with name [{}]", name);

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

    public Result<List<Vehicle>> findByStop(
            final LocalTime stopTime,
            final Integer stopXCoordinate,
            final Integer stopYCoordinate) {

        log.info("Find vehicles arriving at stop with time [{}] and x [{}] y [{}] coordinates",
                stopTime, stopXCoordinate, stopYCoordinate);

        var errorResults = validateStopDetails(stopTime, stopXCoordinate, stopYCoordinate);

        if (errorResults.isEmpty()) {
            var vehicles = vehicleRepository.findByStop(stopTime, stopXCoordinate, stopYCoordinate);
            return Result.ok(vehicles);
        }
        return Result.errors(errorResults);
    }

    private static List<ErrorResult> validateStopDetails(
            final LocalTime stopTime,
            final Integer stopXCoordinate,
            final Integer stopYCoordinate) {
        var errorResults = new ArrayList<ErrorResult>();

        if (stopTime == null) {
            errorResults.add(errorResult("time", "stop time must not be blank"));
        }
        if (stopXCoordinate == null) {
            errorResults.add(errorResult("stopX", "stop X coordinate must not be blank"));
        }
        if (stopYCoordinate == null) {
            errorResults.add(errorResult("stopY", "stop Y coordinate must not be blank"));
        }
        return errorResults;
    }
}