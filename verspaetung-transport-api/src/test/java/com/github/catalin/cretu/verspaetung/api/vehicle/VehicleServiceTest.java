package com.github.catalin.cretu.verspaetung.api.vehicle;

import com.github.catalin.cretu.verspaetung.ErrorResult;
import com.github.catalin.cretu.verspaetung.Populated;
import com.github.catalin.cretu.verspaetung.api.Fixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class VehicleServiceTest {

    @Nested
    @DisplayName("findAll")
    class FindAll {

        @Test
        @DisplayName("With no vehicles - Returns empty")
        void empty() {
            var vehicleService = Fixtures.vehicleService();

            assertThat(vehicleService.findAllVehicles())
                    .isEmpty();
        }

        @Test
        @DisplayName("With existing vehicles - Returns vehicles")
        void allVehicles() {
            var vehicleService = Fixtures.vehicleService(
                    Vehicle.builder().id(22L).build(),
                    Vehicle.builder().id(254L).build());

            assertThat(vehicleService.findAllVehicles())
                    .extracting(Vehicle::getId)
                    .containsExactlyInAnyOrder(254L, 22L);
        }
    }

    @Nested
    @DisplayName("findByLineName")
    class FindByLineName {

        @Test
        @DisplayName("With blank line name - Returns error result")
        void blankLineName() {
            var vehicleService = Fixtures.vehicleService();

            var errors = vehicleService.findByLineName("").getErrors();

            assertThat(errors)
                    .extracting(ErrorResult::getCode, ErrorResult::getMessage)
                    .containsSequence(tuple("lineName", "Line name must not be blank"));
        }

        @Test
        @DisplayName("Vehicles with no line name - Returns empty result")
        void empty() {
            var vehicleService = Fixtures.vehicleService(
                    Populated.vehicle().build(),
                    Populated.vehicle()
                            .line(Populated.line().name("M3").build())
                            .build());

            var vehicles = vehicleService.findByLineName("M3").get();

            assertThat(vehicles.get(0).getLine())
                    .extracting(Line::getName)
                    .isEqualTo("M3");
        }

        @Test
        @DisplayName("Vehicles with line name - Returns vehicles result")
        void vehicles() {
            var vehicleService = Fixtures.vehicleService(
                    Populated.vehicle(0L).build(),
                    Populated.vehicle(1L)
                            .line(Populated.line().name("L1").build())
                            .build(),
                    Populated.vehicle(2L)
                            .line(Populated.line().name("L1").build())
                            .build());

            var vehicles = vehicleService.findByLineName("L1").get();

            assertThat(vehicles)
                    .extracting(Vehicle::getLine)
                    .hasSize(2)
                    .allMatch(line -> line.getName().equals("L1"));
        }
    }


    @Nested
    @DisplayName("findNextAtStop")
    class FindNextAtStop {

        @Test
        @DisplayName("Missing stop id - Returns error result")
        void missingStopId() {
            var vehicleService = Fixtures.vehicleService();

            var errors = vehicleService.findNextAtStop(21L).getErrors();

            assertThat(errors)
                    .extracting(ErrorResult::getCode, ErrorResult::getMessage)
                    .containsSequence(tuple("stopId", "Cannot find stop with ID: 21"));
        }

        @Test
        @DisplayName("Vehicles with no stop id - Returns empty result")
        void empty() {
            var vehicleService = Fixtures.vehicleService(emptyList(), 21L, 30L);

            var vehicles = vehicleService.findNextAtStop(21L).get();

            assertThat(vehicles).isEmpty();
        }

        @Test
        @DisplayName("Next vehicles with stop id - Returns next vehicle result")
        void vehicles() {
            var vehicleService = Fixtures.vehicleService(List.of(
                    Populated.vehicle(0L).line(
                            Populated.line()
                                    .name("line-21")
                                    .delay(0)
                                    .stops(List.of(
                                            Populated.stop()
                                                    .id(1L)
                                                    .time(LocalTime.of(10, 0, 0))
                                                    .build()))
                                    .build())
                            .build(),
                    Populated.vehicle(1L).line(
                            Populated.line()
                                    .name("line-21")
                                    .delay(5)
                                    .stops(List.of(
                                            Populated.stop().id(1L).build(),
                                            Populated.stop()
                                                    .id(21L)
                                                    .time(LocalTime.of(11, 0, 0))
                                                    .build()))
                                    .build())
                            .build(),
                    Populated.vehicle(2L).line(
                            Populated.line()
                                    .name("line-3")
                                    .delay(3)
                                    .stops(List.of(
                                            Populated.stop()
                                                    .id(21L)
                                                    .time(LocalTime.of(11, 0, 0))
                                                    .build(),
                                            Populated.stop().id(31L).build()))
                                    .build())
                            .build()),
                    2L, 3L);

            var vehicles = vehicleService.findNextAtStop(21L).get();

            assertThat(vehicles)
                    .extracting(Vehicle::getLine)
                    .hasSize(2)
                    .extracting(Line::getName)
                    .containsSequence("line-3", "line-21");
        }
    }
}