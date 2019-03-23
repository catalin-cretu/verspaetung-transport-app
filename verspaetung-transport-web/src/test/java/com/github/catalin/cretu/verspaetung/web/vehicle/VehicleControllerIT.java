package com.github.catalin.cretu.verspaetung.web.vehicle;

import com.github.catalin.cretu.verspaetung.api.vehicle.Vehicle;
import com.github.catalin.cretu.verspaetung.api.vehicle.VehicleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static com.github.catalin.cretu.verspaetung.web.Paths.api;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = VehicleController.class)
class VehicleControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VehicleService vehicleService;

    @Nested
    @DisplayName("GET  " + api.Vehicles)
    class FindAll {

        @Test
        @DisplayName("With Vehicles - Returns All Vehicles")
        void returnsAll() throws Exception {
            when(vehicleService.findAllVehicles())
                    .thenReturn(Set.of(
                            Vehicle.builder().id(1L).build(),
                            Vehicle.builder().id(2L).build()));

            mockMvc.perform(
                    get(api.Vehicles))

                    .andExpect(jsonPath("$.errors").isEmpty())
                    .andExpect(jsonPath("$.vehicles.[0].id").value(1))
                    .andExpect(jsonPath("$.vehicles.[1].id").value(2));
        }
    }
}