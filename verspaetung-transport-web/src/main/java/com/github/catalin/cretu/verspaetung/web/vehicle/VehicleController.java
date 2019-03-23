package com.github.catalin.cretu.verspaetung.web.vehicle;

import com.github.catalin.cretu.verspaetung.api.vehicle.Vehicle;
import com.github.catalin.cretu.verspaetung.api.vehicle.VehicleService;
import com.github.catalin.cretu.verspaetung.web.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import static com.github.catalin.cretu.verspaetung.web.Paths.api;
import static java.util.stream.Collectors.toSet;

@Slf4j
@RestController
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(final VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping(path = api.Vehicles)
    public ApiResponse<VehiclesResponse> findAll() {
        Set<Vehicle> vehicles = vehicleService.findAllVehicles();

        return ApiResponse.ok(VehiclesResponse.builder()
                .vehicles(toVehicleViews(vehicles))
                .build());
    }

    private static Set<VehicleView> toVehicleViews(final Set<Vehicle> vehicles) {
        return vehicles.stream()
                .map(vehicle -> VehicleView.builder()
                        .id(vehicle.getId())
                        .build())
                .collect(toSet());
    }
}
