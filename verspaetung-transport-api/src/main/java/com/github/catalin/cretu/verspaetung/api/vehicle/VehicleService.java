package com.github.catalin.cretu.verspaetung.api.vehicle;

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
}