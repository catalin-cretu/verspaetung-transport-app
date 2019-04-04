package com.github.catalin.cretu.verspaetung.jpa;

import java.time.LocalTime;

public interface NextVehicleProjection {

    Long getVehicleId();

    Long getLineId();

    String getLineName();

    LocalTime getTime();

    Integer getDelay();
}
