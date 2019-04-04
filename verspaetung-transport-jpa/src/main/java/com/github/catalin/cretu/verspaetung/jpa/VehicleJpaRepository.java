package com.github.catalin.cretu.verspaetung.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleJpaRepository extends JpaRepository<VehicleEntity, Long> {

    Optional<VehicleEntity> findByLineName(final String lineName);

    boolean existsByLineStopTimesStopId(final Long stopId);

    @Query("select" +
            "  vehicle.id as vehicleId," +
            "  line.id as lineId," +
            "  line.name as lineName," +
            "  stopTimes.time as time," +
            "  lineDelay.delay as delay " +
            "from VehicleEntity vehicle " +
            "join vehicle.line line " +
            "join line.delay lineDelay " +
            "join line.stopTimes stopTimes " +
            "join stopTimes.stop stop " +
            "where " +
            "  stop.id = :stopId " +
            "order by " +
            "  stopTimes.time," +
            "  lineDelay.delay")
    List<NextVehicleProjection> findNextAtStop(@Param("stopId") final Long stopId);
}