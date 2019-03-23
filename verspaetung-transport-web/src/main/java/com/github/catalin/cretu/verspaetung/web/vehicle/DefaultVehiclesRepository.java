package com.github.catalin.cretu.verspaetung.web.vehicle;

import com.github.catalin.cretu.verspaetung.api.vehicle.Line;
import com.github.catalin.cretu.verspaetung.api.vehicle.Stop;
import com.github.catalin.cretu.verspaetung.api.vehicle.Vehicle;
import com.github.catalin.cretu.verspaetung.api.vehicle.VehicleRepository;
import com.github.catalin.cretu.verspaetung.jpa.StopTimeEntity;
import com.github.catalin.cretu.verspaetung.jpa.VehicleEntity;
import com.github.catalin.cretu.verspaetung.jpa.VehicleJpaRepository;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class DefaultVehiclesRepository implements VehicleRepository {

    private final VehicleJpaRepository vehicleJpaRepository;

    DefaultVehiclesRepository(final VehicleJpaRepository vehicleJpaRepository) {
        this.vehicleJpaRepository = vehicleJpaRepository;
    }

    @Override
    public List<Vehicle> findAll() {
        return vehicleJpaRepository.findAll()
                .stream()
                .map(DefaultVehiclesRepository::toVehicle)
                .collect(toList());
    }

    @Override
    public Optional<Vehicle> findByLineName(final String name) {
        return vehicleJpaRepository.findByLineName(name)
                .map(DefaultVehiclesRepository::toVehicle);
    }

    private static Vehicle toVehicle(final VehicleEntity vehicleEntity) {
        var lineEntity = vehicleEntity.getLine();
        var entityDelay = lineEntity.getDelay();

        return Vehicle.builder()
                .id(vehicleEntity.getId())
                .line(Line.builder()
                        .id(lineEntity.getId())
                        .name(lineEntity.getName())
                        .delay(entityDelay != null ? entityDelay.getDelay() : null)
                        .stops(toStops(lineEntity.getStopTimes()))
                        .build())
                .build();
    }

    private static List<Stop> toStops(final List<StopTimeEntity> stopTimes) {
        return stopTimes.stream()
                .map(stopTimeEntity -> Stop.builder()
                        .id(stopTimeEntity.getStop().getId())
                        .time(stopTimeEntity.getTime())
                        .xCoordinate(stopTimeEntity.getStop().getXCoordinate())
                        .yCoordinate(stopTimeEntity.getStop().getYCoordinate())
                        .build())
                .collect(toList());
    }
}
