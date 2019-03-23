package com.github.catalin.cretu.verspaetung.api.vehicle;

import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Data
@Builder
public class Stop {

    private Long id;
    private LocalTime time;
    private Integer xCoordinate;
    private Integer yCoordinate;
}
