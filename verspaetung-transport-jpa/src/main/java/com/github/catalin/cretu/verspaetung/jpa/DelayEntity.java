package com.github.catalin.cretu.verspaetung.jpa;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "delays")
public class DelayEntity {

    @Id
    @Column(name = "line_name")
    private String name;

    @Column(name = "delay")
    private Integer delay;
}
