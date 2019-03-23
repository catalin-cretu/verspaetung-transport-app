package com.github.catalin.cretu.verspaetung.api.vehicle;

import java.util.Set;

public class InMemoryVehicleRepository implements VehicleRepository {

    private Set<Vehicle> vehicles;
    private long id = 0;

    public InMemoryVehicleRepository(final Set<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    @Override
    public Set<Vehicle> findAll() {
        return vehicles;
    }
}
