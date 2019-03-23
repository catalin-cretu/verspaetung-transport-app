package com.github.catalin.cretu.verspaetung.jpa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "lines")
public class LineEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "line_id")
    private Long id;

    @Column(name = "line_name")
    private String name;

    @OneToOne()
    @JoinColumn(name = "line_name", insertable = false, updatable = false)
    private DelayEntity delay;
}
