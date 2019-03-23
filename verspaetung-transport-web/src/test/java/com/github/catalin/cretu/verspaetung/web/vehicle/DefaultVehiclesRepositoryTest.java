package com.github.catalin.cretu.verspaetung.web.vehicle;


import com.github.catalin.cretu.verspaetung.api.vehicle.Vehicle;
import com.github.catalin.cretu.verspaetung.jpa.VehicleEntity;
import com.github.catalin.cretu.verspaetung.jpa.VehicleJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DefaultVehiclesRepositoryTest {

    private VehicleJpaRepository vehicleJpaRepository = mock(VehicleJpaRepository.class);

    private DefaultVehiclesRepository vehiclesRepository;

    @BeforeEach
    void setup() {
        vehiclesRepository = new DefaultVehiclesRepository(vehicleJpaRepository);
    }

    @Test
    @DisplayName("findAll - Calls jpa repository")
    void findAll_CallJpaRepo() {
        vehiclesRepository.findAll();

        verify(vehicleJpaRepository).findAll();
    }

    @Test
    @DisplayName("findAll - Returns vehicles")
    void findAll_FindsVehicleEntities() {
        when(vehicleJpaRepository.findAll())
                .thenReturn(List.of(
                        VehicleEntity.builder().id(45L).build(),
                        VehicleEntity.builder().id(22L).build(),
                        VehicleEntity.builder().id(88L).build()));

        Set<Vehicle> vehicles = vehiclesRepository.findAll();

        assertThat(vehicles)
                .extracting(Vehicle::getId)
                .containsExactlyInAnyOrder(45L, 22L, 88L);
    }
}