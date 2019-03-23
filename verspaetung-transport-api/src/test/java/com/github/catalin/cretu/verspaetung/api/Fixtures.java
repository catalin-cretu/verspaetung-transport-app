package com.github.catalin.cretu.verspaetung.api;

import com.github.catalin.cretu.verspaetung.api.vehicle.InMemoryVehicleRepository;
import com.github.catalin.cretu.verspaetung.api.vehicle.Vehicle;
import com.github.catalin.cretu.verspaetung.api.vehicle.VehicleService;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class Fixtures {

    public static VehicleService vehicleService(final List<Vehicle> vehicles) {
        return new VehicleService(new InMemoryVehicleRepository(vehicles));
    }
}
