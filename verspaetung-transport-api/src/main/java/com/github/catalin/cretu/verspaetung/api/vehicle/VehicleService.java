package com.github.catalin.cretu.verspaetung.api.vehicle;

import com.github.catalin.cretu.verspaetung.Result;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(final VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Set<Vehicle> findAllVehicles() {
        log.debug("Find all vehicles");

        return vehicleRepository.findAll();
    }

    public Result<Set<Vehicle>> findByLineName(final String name) {
        if (name.isBlank()) {
            return Result.error("lineName", "Line name must not be blank");
        }
        log.info("Find vehicles with line with name [{}]", name);

        var optionalVehicle = vehicleRepository.findByLineName(name);
        return optionalVehicle
                .map(vehicle -> Result.ok(Set.of(vehicle)))
                .orElse(Result.ok(Set.of()));
    }
}