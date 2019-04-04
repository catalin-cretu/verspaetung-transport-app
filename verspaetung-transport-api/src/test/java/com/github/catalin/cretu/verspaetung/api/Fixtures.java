package com.github.catalin.cretu.verspaetung.api;

import com.github.catalin.cretu.verspaetung.api.vehicle.InMemoryVehicleRepository;
import com.github.catalin.cretu.verspaetung.api.vehicle.Vehicle;
import com.github.catalin.cretu.verspaetung.api.vehicle.VehicleService;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;

@UtilityClass
public class Fixtures {

    public static VehicleService vehicleService(final Vehicle... vehicles) {
        return new VehicleService(new InMemoryVehicleRepository(emptyList(), Arrays.asList(vehicles)));
    }

    public static VehicleService vehicleService(final List<Vehicle> vehicles, final Long... stopIds) {
        return new VehicleService(new InMemoryVehicleRepository(Arrays.asList(stopIds), vehicles));
    }
}