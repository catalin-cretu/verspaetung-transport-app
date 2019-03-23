package com.github.catalin.cretu.verspaetung.web.vehicle;

import com.github.catalin.cretu.verspaetung.api.vehicle.Vehicle;
import com.github.catalin.cretu.verspaetung.api.vehicle.VehicleRepository;
import com.github.catalin.cretu.verspaetung.jpa.VehicleEntity;
import com.github.catalin.cretu.verspaetung.jpa.VehicleJpaRepository;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class DefaultVehiclesRepository implements VehicleRepository {

    private final VehicleJpaRepository vehicleJpaRepository;

    DefaultVehiclesRepository(final VehicleJpaRepository vehicleJpaRepository) {
        this.vehicleJpaRepository = vehicleJpaRepository;
    }

    @Override
    public Set<Vehicle> findAll() {
        return vehicleJpaRepository.findAll()
                .stream()
                .map(DefaultVehiclesRepository::toVehicle)
                .collect(toSet());
    }

    private static Vehicle toVehicle(final VehicleEntity vehicleEntity) {
        return Vehicle.builder()
                .id(vehicleEntity.getId())
                .build();
    }
}
