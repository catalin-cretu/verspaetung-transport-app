package com.github.catalin.cretu.verspaetung.api.vehicle;

import java.util.Optional;
import java.util.Set;

public interface VehicleRepository {

    Set<Vehicle> findAll();

    Optional<Vehicle> findByLineName(final String name);
}
