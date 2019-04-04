package com.github.catalin.cretu.verspaetung;

import com.github.catalin.cretu.verspaetung.api.vehicle.Line;
import com.github.catalin.cretu.verspaetung.api.vehicle.Stop;
import com.github.catalin.cretu.verspaetung.api.vehicle.Vehicle;
import lombok.experimental.UtilityClass;

import java.time.LocalTime;
import java.util.List;

@UtilityClass
public class Populated {

    public static Vehicle.VehicleBuilder vehicle() {
        return vehicle(-11L);
    }

    public static Vehicle.VehicleBuilder vehicle(final Long id) {
        return Vehicle.builder()
                .id(id)
                .line(line().build());
    }

    public static Line.LineBuilder line() {
        return Line.builder()
                .id(-20L)
                .delay(2)
                .name("S1")
                .stops(List.of(
                        stop()
                                .build()));
    }

    public static Stop.StopBuilder stop() {
        return Stop.builder()
                .id(-30L)
                .time(LocalTime.of(20, 1, 0))
                .xCoordinate(1)
                .yCoordinate(1);
    }
}
