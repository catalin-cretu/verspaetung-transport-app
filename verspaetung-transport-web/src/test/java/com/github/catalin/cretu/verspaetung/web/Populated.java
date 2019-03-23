package com.github.catalin.cretu.verspaetung.web;

import com.github.catalin.cretu.verspaetung.api.vehicle.Line;
import com.github.catalin.cretu.verspaetung.api.vehicle.Vehicle;
import com.github.catalin.cretu.verspaetung.jpa.LineEntity;
import com.github.catalin.cretu.verspaetung.jpa.VehicleEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Populated {

    public Vehicle.VehicleBuilder vehicle() {
        return Vehicle.builder()
                .id(1L)
                .line(line().build());
    }

    public static Line.LineBuilder line() {
        return Line.builder()
                .id(2L)
                .name("M4");
    }

    public static VehicleEntity.VehicleEntityBuilder vehicleEntity() {
        return VehicleEntity.builder()
                .id(1L)
                .line(lineEntity().build());
    }

    public static LineEntity.LineEntityBuilder lineEntity() {
        return LineEntity.builder()
                .id(2L)
                .name("m4");
    }
}