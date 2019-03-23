package com.github.catalin.cretu.verspaetung.web.vehicle;


import com.github.catalin.cretu.verspaetung.api.vehicle.Line;
import com.github.catalin.cretu.verspaetung.api.vehicle.Stop;
import com.github.catalin.cretu.verspaetung.api.vehicle.Vehicle;
import com.github.catalin.cretu.verspaetung.jpa.LineEntity;
import com.github.catalin.cretu.verspaetung.jpa.VehicleJpaRepository;
import com.github.catalin.cretu.verspaetung.web.Populated;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
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
    @DisplayName("findByLineName - Calls jpa repository")
    void findByLineName_CallJpaRepo() {
        vehiclesRepository.findByLineName("22");

        verify(vehicleJpaRepository).findByLineName("22");
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
                        Populated.vehicleEntity().id(45L).build(),
                        Populated.vehicleEntity().id(22L).build(),
                        Populated.vehicleEntity()
                                .id(88L)
                                .line(LineEntity.builder()
                                        .id(10L)
                                        .name("SPA")
                                        .delay(Populated.delay()
                                                .name("SPA")
                                                .delay(3)
                                                .build())
                                        .stopTimes(List.of(Populated.stopTimeEntity()
                                                .time(LocalTime.of(14, 0, 4))
                                                .stop(Populated.stopEntity()
                                                        .id(988L)
                                                        .xCoordinate(55)
                                                        .yCoordinate(1)
                                                        .build())
                                                .build()))
                                        .build())
                                .build()));

        var vehicles = vehiclesRepository.findAll();

        assertThat(vehicles)
                .extracting(Vehicle::getId)
                .containsExactlyInAnyOrder(45L, 22L, 88L);

        var vehicle3 = vehicles.stream()
                .filter(vehicle -> vehicle.getId().equals(88L))
                .findFirst()
                .get();

        assertThat(vehicle3.getLine())
                .extracting(Line::getId, Line::getName, Line::getDelay)
                .containsSequence(10L, "SPA", 3);

        assertThat(vehicle3.getLine().getStops())
                .extracting(Stop::getId, Stop::getTime, Stop::getXCoordinate, Stop::getYCoordinate)
                .containsSequence(tuple(988L, LocalTime.of(14, 0, 4), 55, 1));
    }
}