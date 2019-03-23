package com.github.catalin.cretu.verspaetung.jpa;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "stops")
public class StopEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "stop_id")
    private Long id;

    @Column(name = "x")
    private Integer xCoordinate;

    @Column(name = "y")
    private Integer yCoordinate;

}
