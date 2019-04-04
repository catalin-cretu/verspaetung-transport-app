package com.github.catalin.cretu.verspaetung.web;

import com.github.catalin.cretu.verspaetung.jpa.DelayEntity;
import com.github.catalin.cretu.verspaetung.jpa.LineEntity;
import com.github.catalin.cretu.verspaetung.jpa.StopEntity;
import com.github.catalin.cretu.verspaetung.jpa.StopTimeEntity;
import com.github.catalin.cretu.verspaetung.jpa.VehicleEntity;
import com.github.catalin.cretu.verspaetung.web.vehicle.NextVehicleProjection;
import lombok.experimental.UtilityClass;

import java.time.LocalTime;
import java.util.List;

@UtilityClass
public class Populated {

    public static VehicleEntity.VehicleEntityBuilder vehicleEntity() {
        return VehicleEntity.builder()
                .id(1L)
                .line(lineEntity().build());
    }

    public static LineEntity.LineEntityBuilder lineEntity() {
        return LineEntity.builder()
                .id(2L)
                .name("m4")
                .delay(delay().build())
                .stopTimes(List.of(Populated.stopTimeEntity().build()));
    }

    public static DelayEntity.DelayEntityBuilder delay() {
        return DelayEntity.builder()
                .delay(22)
                .name("m4");
    }

    public static StopTimeEntity.StopTimeEntityBuilder stopTimeEntity() {
        return StopTimeEntity.builder()
                .time(LocalTime.of(10, 10, 10))
                .stop(stopEntity().build());
    }

    public static StopEntity.StopEntityBuilder stopEntity() {
        return StopEntity.builder()
                .id(3L)
                .xCoordinate(1)
                .yCoordinate(2);
    }

    public static NextVehicleProjection.NextVehicleProjectionBuilder nextVehicle() {
        return NextVehicleProjection.builder()
                .vehicleId(45L)
                .lineId(11L)
                .lineName("T1")
                .delay(2)
                .time(LocalTime.of(10, 10, 10));
    }
}