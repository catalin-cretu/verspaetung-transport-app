package com.github.catalin.cretu.verspaetung.jpa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@JpaTestExtension
class VehicleJpaRepositoryIT {

    @Autowired
    private VehicleJpaRepository vehicleJpaRepository;
    @Autowired
    private LineJpaRepository lineJpaRepository;

    @Test
    @DisplayName("findAll - Returns vehicle entities")
    void findAll() {
        assertThat(vehicleJpaRepository.findAll()).isEmpty();

        var lineEntity1 = lineJpaRepository.saveAndFlush(LineEntity.builder().name("L1").build());
        var lineEntity2 = lineJpaRepository.saveAndFlush(LineEntity.builder().name("L2").build());

        var savedVehicleEntity1 = vehicleJpaRepository.saveAndFlush(VehicleEntity.builder()
                .line(lineEntity1)
                .build());
        var savedVehicleEntity2 = vehicleJpaRepository.saveAndFlush(VehicleEntity.builder()
                .line(lineEntity2)
                .build());

        var vehicleEntities = vehicleJpaRepository.findAll();
        assertThat(vehicleEntities)
                .extracting(VehicleEntity::getId)
                .containsExactlyInAnyOrder(savedVehicleEntity1.getId(), savedVehicleEntity2.getId());

        var vehicleEntity1 = vehicleEntities.stream()
                .filter(vehicle -> vehicle.getId().equals(savedVehicleEntity1.getId()))
                .findFirst()
                .get();

        assertThat(vehicleEntity1.getLine())
                .extracting(LineEntity::getId, LineEntity::getName)
                .containsSequence(lineEntity1.getId(), "L1");
    }

    @Test
    @DisplayName("findByLineName - Returns vehicle by line name")
    void findByLineName() {
        assertThat(vehicleJpaRepository.findByLineName("not found")).isEmpty();

        var lineEntity = lineJpaRepository.saveAndFlush(LineEntity.builder().name("M1").build());

        vehicleJpaRepository.saveAndFlush(VehicleEntity.builder()
                .line(lineEntity)
                .build());

        var optionalVehicleEntity = vehicleJpaRepository.findByLineName("M1");

        assertThat(optionalVehicleEntity).isPresent();
        assertThat(optionalVehicleEntity.get().getLine().getName())
                .isEqualTo("M1");
    }
}