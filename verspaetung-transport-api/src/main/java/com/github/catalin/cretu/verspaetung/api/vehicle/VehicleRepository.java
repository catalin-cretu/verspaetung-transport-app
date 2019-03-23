package com.github.catalin.cretu.verspaetung.api.vehicle;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository {

    List<Vehicle> findAll();

    Optional<Vehicle> findByLineName(final String name);

}
