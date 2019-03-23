package com.github.catalin.cretu.verspaetung.web.vehicle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StopView {

    private Long id;
    private LocalTime time;
    private CoordinatesView coordinates;
}
