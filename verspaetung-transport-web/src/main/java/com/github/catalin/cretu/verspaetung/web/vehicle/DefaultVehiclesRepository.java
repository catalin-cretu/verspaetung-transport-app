package com.github.catalin.cretu.verspaetung.web.vehicle;

import com.github.catalin.cretu.verspaetung.api.vehicle.Line;
import com.github.catalin.cretu.verspaetung.api.vehicle.Stop;
import com.github.catalin.cretu.verspaetung.api.vehicle.Vehicle;
import com.github.catalin.cretu.verspaetung.api.vehicle.VehicleRepository;
import com.github.catalin.cretu.verspaetung.jpa.NextVehicleProjection;
import com.github.catalin.cretu.verspaetung.jpa.StopTimeEntity;
import com.github.catalin.cretu.verspaetung.jpa.VehicleEntity;
import com.github.catalin.cretu.verspaetung.jpa.VehicleJpaRepository;

import java.util.List;

import static java.util.Collections.emptyList;
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
    public List<Vehicle> findByLineName(final String name) {
        return vehicleJpaRepository.findByLineName(name)
                .stream()
                .map(DefaultVehiclesRepository::toVehicle)
                .collect(toList());
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

    @Override
    public boolean stopIdExists(final Long stopId) {
        return vehicleJpaRepository.existsByLineStopTimesStopId(stopId);
    }

    @Override
    public List<Vehicle> findNextArrivingAtStop(final Long stopId) {
        var nextVehicleProjections = vehicleJpaRepository.findNextAtStop(stopId);

        return nextVehicleProjections.stream()
                .sorted(DefaultVehiclesRepository::compareDelayedTime)
                .map(DefaultVehiclesRepository::convertProjectionToVehicle)
                .collect(toList());
    }

    private static int compareDelayedTime(
            final NextVehicleProjection first,
            final NextVehicleProjection second) {
        var firstDelayedTime = first.getTime().plusMinutes(first.getDelay());
        var secondDelayedTime = second.getTime().plusMinutes(second.getDelay());

        return firstDelayedTime.compareTo(secondDelayedTime);
    }

    private static Vehicle convertProjectionToVehicle(final NextVehicleProjection vehicleProjection) {
        return Vehicle.builder()
                .id(vehicleProjection.getVehicleId())
                .line(Line.builder()
                        .id(vehicleProjection.getLineId())
                        .name(vehicleProjection.getLineName())
                        .stops(emptyList())
                        .build())
                .build();
    }
}
