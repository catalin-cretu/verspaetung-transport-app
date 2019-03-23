package com.github.catalin.cretu.verspaetung.web.vehicle;

import com.github.catalin.cretu.verspaetung.api.vehicle.VehicleService;
import com.github.catalin.cretu.verspaetung.jpa.VehicleJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VehicleConfiguration {

    @Bean
    public VehicleService vehicleService(final VehicleJpaRepository vehicleJpaRepository) {
        return new VehicleService(new DefaultVehiclesRepository(vehicleJpaRepository));
    }
}