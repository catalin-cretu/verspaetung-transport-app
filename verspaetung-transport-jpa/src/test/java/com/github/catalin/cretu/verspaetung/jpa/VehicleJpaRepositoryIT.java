package com.github.catalin.cretu.verspaetung.jpa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@JpaTestExtension
class VehicleJpaRepositoryIT {

    @Autowired
    private VehicleJpaRepository vehicleJpaRepository;

    @Test
    @DisplayName("findAll - Returns vehicle entities")
    void findAll() {
        assertThat(vehicleJpaRepository.findAll()).isEmpty();

        var firstVehicleId = vehicleJpaRepository.saveAndFlush(VehicleEntity.builder().build()).getId();
        var secondVehicleId = vehicleJpaRepository.saveAndFlush(VehicleEntity.builder().build()).getId();

        assertThat(vehicleJpaRepository.findAll())
                .extracting(VehicleEntity::getId)
                .containsSequence(firstVehicleId, secondVehicleId);
    }
}