package com.github.catalin.cretu.verspaetung.jpa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;

@JpaTestExtension
class VehicleJpaRepositoryIT {

    @Autowired
    private VehicleJpaRepository vehicleJpaRepository;
    @Autowired
    private DelayJpaRepository delayJpaRepository;
    @Autowired
    private LineJpaRepository lineJpaRepository;
    @Autowired
    private StopJpaRepository stopJpaRepository;

    @Test
    @DisplayName("findAll - Returns vehicle entities")
    void findAll() {
        vehicleJpaRepository.deleteAll();

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
                .orElseThrow();

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
        assertThat(optionalVehicleEntity.orElseThrow().getLine().getName())
                .isEqualTo("M1");
    }

    @Test
    @DisplayName("existsByStopId - Returns true")
    void existsByStopId() {
        assertThat(vehicleJpaRepository.existsByLineStopTimesStopId(-2342L))
                .isFalse();

        VehicleEntity vehicleEntity = createVehicle(2, "10:00:00");

        var stopId = vehicleEntity.getLine().getStopTimes().get(0).getStop().getId();
        assertThat(vehicleJpaRepository.existsByLineStopTimesStopId(stopId))
                .isTrue();
    }

    @Test
    @DisplayName("findNextAtStop - Returns vehicles ordered by stop time and delay")
    void findNextArrivingAtStop() {
        assertThat(vehicleJpaRepository.findNextAtStop(-23423L))
                .isEmpty();

        List<VehicleEntity> savedVehicles = createVehicles(
                entry(4, "10:00"),
                entry(1, "10:01"),
                entry(1, "10:02"));

        var stopId = savedVehicles.get(0).getLine().getStopTimes().get(0).getStop().getId();

        List<NextVehicleProjection> nextVehicleProjections = vehicleJpaRepository.findNextAtStop(stopId);
        assertThat(nextVehicleProjections)
                .hasSize(3);

        var firstVehicleProjection = nextVehicleProjections.get(0);
        var firstVehicle = savedVehicles.get(0);
        assertThat(firstVehicleProjection.getVehicleId())
                .isEqualTo(firstVehicle.getId());

        assertThat(firstVehicleProjection.getTime().toString())
                .isEqualTo("10:00");
        assertThat(firstVehicleProjection.getDelay())
                .isEqualTo(4);

        var secondVehicleProjection = nextVehicleProjections.get(1);
        var secondVehicle = savedVehicles.get(1);
        assertThat(secondVehicleProjection.getVehicleId())
                .isEqualTo(secondVehicle.getId());

        assertThat(secondVehicleProjection.getTime().toString())
                .isEqualTo("10:01");
        assertThat(secondVehicleProjection.getDelay())
                .isEqualTo(1);

        var thirdVehicleProjection = nextVehicleProjections.get(2);
        var thirdVehicle = savedVehicles.get(2);
        assertThat(thirdVehicleProjection.getVehicleId())
                .isEqualTo(thirdVehicle.getId());

        assertThat(thirdVehicleProjection.getTime().toString())
                .isEqualTo("10:02");
        assertThat(thirdVehicleProjection.getDelay())
                .isEqualTo(1);
    }

    @Test
    @DisplayName("findByStop - Returns vehicles ordered by stop time and delay")
    void findByStop() {
        var stopEntity = stopJpaRepository.saveAndFlush(StopEntity.builder()
                .id(54578L)
                .xCoordinate(44)
                .yCoordinate(66)
                .build());
        var savedVehicleEntity = createVehicle(stopEntity, 0, "12:02:03");

        List<VehicleEntity> vehicleEntities =
                vehicleJpaRepository.findByStop(LocalTime.of(12, 2, 3), 44, 66);

        var vehicleEntity = vehicleEntities.get(0);
        assertThat(vehicleEntity.getId())
                .isEqualTo(savedVehicleEntity.getId());

        var stopTimeEntity = vehicleEntity.getLine().getStopTimes().get(0);
        assertThat(stopTimeEntity.getTime())
                .isEqualTo(LocalTime.of(12, 2, 3));

        assertThat(stopTimeEntity.getStop())
                .extracting(StopEntity::getXCoordinate, StopEntity::getYCoordinate)
                .containsSequence(44, 66);
    }

    @SafeVarargs
    private List<VehicleEntity> createVehicles(
            final Entry<Integer, String>... delayAndTimes) {
        var savedVehicles = new ArrayList<VehicleEntity>();
        var stopEntity = stopJpaRepository.saveAndFlush(StopEntity.builder().build());

        for (var delayAndTimeEntry : delayAndTimes) {
            Integer delay = delayAndTimeEntry.getKey();
            String time = delayAndTimeEntry.getValue();

            savedVehicles.add(createVehicle(stopEntity, delay, time));
        }
        return savedVehicles;
    }

    private VehicleEntity createVehicle(final Integer delay, final String time) {
        return createVehicle(null, delay, time);
    }

    private VehicleEntity createVehicle(StopEntity stop, final Integer delay, final String time) {
        String lineName = "L" + delay;
        var lineEntity = lineJpaRepository.saveAndFlush(
                LineEntity.builder()
                        .name(lineName)
                        .stopTimes(new ArrayList<>())
                        .build());

        StopEntity stopEntity = stop;
        if (stopEntity == null) {
            stopEntity = stopJpaRepository.saveAndFlush(StopEntity.builder().build());
        }

        lineJpaRepository.saveStopTimeEntity(lineEntity.getId(), stopEntity.getId(), time);
        var stopTimeEntity = lineJpaRepository.findStopTimeEntity(stopEntity.getId(), toLocalTime(time));

        var delayEntity = delayJpaRepository.saveAndFlush(
                DelayEntity.builder()
                        .delay(delay)
                        .name(lineName)
                        .build());
        lineEntity.setDelay(delayEntity);
        lineEntity.getStopTimes().add(stopTimeEntity);
        lineEntity = lineJpaRepository.saveAndFlush(lineEntity);

        return vehicleJpaRepository.saveAndFlush(
                VehicleEntity.builder()
                        .line(lineEntity)
                        .build());
    }

    private static LocalTime toLocalTime(final String time) {
        return LocalTime.parse(time);
    }
}