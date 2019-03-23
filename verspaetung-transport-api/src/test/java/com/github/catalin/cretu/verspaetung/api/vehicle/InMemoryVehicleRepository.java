package com.github.catalin.cretu.verspaetung.api.vehicle;

import java.util.List;
import java.util.Optional;

public class InMemoryVehicleRepository implements VehicleRepository {

    private List<Vehicle> vehicles;
    private long id = 0;

    public InMemoryVehicleRepository(final List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    @Override
    public List<Vehicle> findAll() {
        return vehicles;
    }

    @Override
    public Optional<Vehicle> findByLineName(final String name) {
        return Optional.empty();
    }

}
