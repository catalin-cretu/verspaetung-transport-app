package com.github.catalin.cretu.verspaetung.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StopJpaRepository extends JpaRepository<StopEntity, Long> {
    //no-op
}