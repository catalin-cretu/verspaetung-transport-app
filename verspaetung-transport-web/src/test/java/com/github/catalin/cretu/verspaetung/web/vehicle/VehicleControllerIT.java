package com.github.catalin.cretu.verspaetung.web.vehicle;

import com.github.catalin.cretu.verspaetung.Result;
import com.github.catalin.cretu.verspaetung.api.vehicle.VehicleService;
import com.github.catalin.cretu.verspaetung.web.Populated;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static com.github.catalin.cretu.verspaetung.web.Paths.Params;
import static com.github.catalin.cretu.verspaetung.web.Paths.api;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
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
        @DisplayName("Returns multiple vehicles")
        void returnsAll() throws Exception {
            when(vehicleService.findAllVehicles())
                    .thenReturn(Set.of(
                            Populated.vehicle().id(1L).build(),
                            Populated.vehicle().id(2L).build()));

            mockMvc.perform(
                    get(api.Vehicles))

                    .andExpect(jsonPath("$.errors").isEmpty())
                    .andExpect(jsonPath("$.vehicles", hasSize(2)));
        }

        @Test
        @DisplayName("Returns vehicles with details")
        void vehicleDetails() throws Exception {
            when(vehicleService.findAllVehicles())
                    .thenReturn(Set.of(
                            Populated.vehicle()
                                    .id(24L)
                                    .line(Populated.line()
                                            .id(55L)
                                            .name("SOS")
                                            .delay(5)
                                            .build())
                                    .build()));

            mockMvc.perform(
                    get(api.Vehicles))

                    .andExpect(jsonPath("$.errors").isEmpty())
                    .andExpect(jsonPath("$.vehicles", hasSize(1)))
                    .andExpect(jsonPath("$.vehicles.[0].id").value(24))
                    .andExpect(jsonPath("$.vehicles.[0].line.id").value(55))
                    .andExpect(jsonPath("$.vehicles.[0].line.name").value("SOS"))
                    .andExpect(jsonPath("$.vehicles.[0].line.delay").value(5));
        }
    }

    @Nested
    @DisplayName("GET  " + api.Vehicles + " ?" + Params.lineName)
    class FindByLineName {

        @Test
        @DisplayName("Returns vehicles with line name")
        void returnsByLineName() throws Exception {
            when(vehicleService.findByLineName("S44"))
                    .thenReturn(Result.ok(
                            Set.of(Populated.vehicle()
                                    .line(Populated.line()
                                            .name("S44")
                                            .build())
                                    .build())));

            mockMvc.perform(
                    get(api.Vehicles).param(Params.lineName, "S44"))

                    .andExpect(jsonPath("$.errors").isEmpty())
                    .andExpect(jsonPath("$.vehicles", hasSize(1)))
                    .andExpect(jsonPath("$.vehicles.[0].line.name").value("S44"));
        }

        @Test
        @DisplayName("Blank line name - Returns error")
        void noLineName() throws Exception {
            when(vehicleService.findByLineName(anyString()))
                    .thenReturn(Result.error("name", "must not be blank"));

            mockMvc.perform(
                    get(api.Vehicles).param(Params.lineName, "S44"))

                    .andExpect(jsonPath("$.errors.[0].code").value("name"))
                    .andExpect(jsonPath("$.errors.[0].message").value("must not be blank"))
                    .andExpect(jsonPath("$.vehicles").isEmpty());
        }
    }
}