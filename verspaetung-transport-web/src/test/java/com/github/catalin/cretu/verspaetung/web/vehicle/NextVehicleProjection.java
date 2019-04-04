package com.github.catalin.cretu.verspaetung.web.vehicle;

import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Data
@Builder
public class NextVehicleProjection implements com.github.catalin.cretu.verspaetung.jpa.NextVehicleProjection {

    private Long vehicleId;
    private Long lineId;
    private String lineName;
    private LocalTime time;
    private Integer delay;
}
