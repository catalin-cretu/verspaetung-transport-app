package com.github.catalin.cretu.verspaetung.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;

@Repository
public interface LineJpaRepository extends JpaRepository<LineEntity, Long> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(nativeQuery = true,
            value = "insert into times values (null, :line_id, :stop_id, :time)")
    void saveStopTimeEntity(
            @Param("line_id") final Long lineId,
            @Param("stop_id") final Long stopId,
            @Param("time") final String time);

    @Query("select stopTime from StopTimeEntity stopTime " +
            "where stopTime.stop.id = :stopId " +
            "and stopTime.time = :time")
    StopTimeEntity findStopTimeEntity(
            @Param("stopId") final Long id,
            @Param("time") final LocalTime time);

}