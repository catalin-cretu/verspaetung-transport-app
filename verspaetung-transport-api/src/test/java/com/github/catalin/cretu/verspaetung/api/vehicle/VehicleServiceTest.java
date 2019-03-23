package com.github.catalin.cretu.verspaetung.api.vehicle;

import com.github.catalin.cretu.verspaetung.api.Fixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class VehicleServiceTest {

    @Test
    @DisplayName("findAll - No vehicles - Returns empty")
    void findAll_Empty() {
        var vehicleService = Fixtures.vehicleService(List.of());

        assertThat(vehicleService.findAllVehicles()).isEmpty();
    }

    @Test
    @DisplayName("findAll - With existing vehicles - Returns vehicles")
    void findAll_Vehicles() {
        var vehicleService = Fixtures.vehicleService(List.of(
                Vehicle.builder().id(22L).build(),
                Vehicle.builder().id(254L).build()));

        assertThat(vehicleService.findAllVehicles())
                .extracting(Vehicle::getId)
                .containsExactlyInAnyOrder(254L, 22L);
    }
}