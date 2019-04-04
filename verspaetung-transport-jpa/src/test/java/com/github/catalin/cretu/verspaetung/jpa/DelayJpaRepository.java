package com.github.catalin.cretu.verspaetung.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DelayJpaRepository extends JpaRepository<DelayEntity, Long> {
    // no-op
}