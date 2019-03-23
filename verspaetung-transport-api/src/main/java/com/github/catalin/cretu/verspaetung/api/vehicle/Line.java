package com.github.catalin.cretu.verspaetung.api.vehicle;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Line {

    private Long id;
    private String name;
    private Integer delay;
}
