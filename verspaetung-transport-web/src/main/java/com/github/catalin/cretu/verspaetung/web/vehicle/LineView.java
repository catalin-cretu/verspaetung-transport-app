package com.github.catalin.cretu.verspaetung.web.vehicle;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LineView {

    private Long id;
    private String name;
    private Integer delay;
}
