package com.github.catalin.cretu.verspaetung.web.vehicle;

import com.github.catalin.cretu.verspaetung.jpa.DelayEntity;
import com.github.catalin.cretu.verspaetung.jpa.StopEntity;
import com.github.catalin.cretu.verspaetung.jpa.VehicleJpaRepository;
import com.github.catalin.cretu.verspaetung.web.Populated;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.github.catalin.cretu.verspaetung.web.Paths.Params;
import static com.github.catalin.cretu.verspaetung.web.Paths.api;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = VehicleController.class)
@Import(VehicleConfiguration.class)
class VehicleControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VehicleJpaRepository vehicleJpaRepository;

    @Nested
    @DisplayName("GET  " + api.Vehicles)
    class FindAll {

        @Test
        @DisplayName("Returns multiple vehicles")
        void returnsAll() throws Exception {
            when(vehicleJpaRepository.findAll())
                    .thenReturn(List.of(
                            Populated.vehicleEntity().id(1L).build(),
                            Populated.vehicleEntity().id(2L).build()));

            mockMvc.perform(
                    get(api.Vehicles))

                    .andExpect(jsonPath("$.errors").isEmpty())
                    .andExpect(jsonPath("$.vehicles", hasSize(2)));
        }

        @Test
        @DisplayName("Returns vehicles with details")
        void vehicleDetails() throws Exception {
            when(vehicleJpaRepository.findAll())
                    .thenReturn(List.of(
                            Populated.vehicleEntity()
                                    .id(24L)
                                    .line(Populated.lineEntity()
                                            .id(55L)
                                            .name("SOS")
                                            .delay(DelayEntity.builder().delay(5).build())
                                            .stopTimes(List.of(
                                                    Populated.stopTimeEntity()
                                                            .time(LocalTime.of(12, 34, 56))
                                                            .stop(StopEntity.builder()
                                                                    .id(18L)
                                                                    .xCoordinate(1)
                                                                    .yCoordinate(10)
                                                                    .build())
                                                            .build()))
                                            .build())
                                    .build()));

            mockMvc.perform(
                    get(api.Vehicles))

                    .andExpect(jsonPath("$.errors").isEmpty())
                    .andExpect(jsonPath("$.vehicles", hasSize(1)))
                    .andExpect(jsonPath("$.vehicles.[0].id").value(24))
                    .andExpect(jsonPath("$.vehicles.[0].line.id").value(55))
                    .andExpect(jsonPath("$.vehicles.[0].line.name").value("SOS"))
                    .andExpect(jsonPath("$.vehicles.[0].line.delay").value(5))
                    .andExpect(jsonPath("$.vehicles.[0].line.stops", hasSize(1)))
                    .andExpect(jsonPath("$.vehicles.[0].line.stops.[0].id").value(18))
                    .andExpect(jsonPath("$.vehicles.[0].line.stops.[0].time").value("12:34:56"))
                    .andExpect(jsonPath("$.vehicles.[0].line.stops.[0].coordinates.x").value(1))
                    .andExpect(jsonPath("$.vehicles.[0].line.stops.[0].coordinates.y").value(10));
        }
    }

    @Nested
    @DisplayName("GET  " + api.Vehicles + " ?" + Params.lineName)
    class FindByLineName {

        @Test
        @DisplayName("Returns vehicles with line name")
        void returnsByLineName() throws Exception {
            when(vehicleJpaRepository.findByLineName("S44"))
                    .thenReturn(Optional.of(
                            Populated.vehicleEntity()
                                    .line(Populated.lineEntity()
                                            .name("S44")
                                            .build())
                                    .build()));

            mockMvc.perform(
                    get(api.Vehicles).param(Params.lineName, "S44"))

                    .andExpect(jsonPath("$.errors").isEmpty())
                    .andExpect(jsonPath("$.vehicles", hasSize(1)))
                    .andExpect(jsonPath("$.vehicles.[0].line.name").value("S44"));
        }

        @Test
        @DisplayName("Blank line name - Returns error")
        void noLineName() throws Exception {
            mockMvc.perform(
                    get(api.Vehicles).param(Params.lineName, ""))

                    .andExpect(jsonPath("$.errors.[0].code").value("lineName"))
                    .andExpect(jsonPath("$.errors.[0].message").value("Line name must not be blank"))
                    .andExpect(jsonPath("$.vehicles").isEmpty());
        }
    }

    @Nested
    @DisplayName("GET  " + api.Vehicles + " ?" + Params.nextAtStop)
    class NextArrivingByStopId {

        @Test
        @DisplayName("Returns next vehicle arriving at stop")
        void nextByStopId() throws Exception {
            when(vehicleJpaRepository.existsByLineStopTimesStopId(any()))
                    .thenReturn(true);
            when(vehicleJpaRepository.findNextAtStop(any()))
                    .thenReturn(List.of(
                            Populated.nextVehicle()
                                    .lineName("third")
                                    .delay(3)
                                    .time(LocalTime.of(12, 1, 1))
                                    .build(),
                            Populated.nextVehicle()
                                    .lineName("second")
                                    .delay(1)
                                    .time(LocalTime.of(12, 1, 1))
                                    .build(),
                            Populated.nextVehicle()
                                    .lineName("first")
                                    .delay(1)
                                    .time(LocalTime.of(12, 1, 0))
                                    .build()));

            mockMvc.perform(
                    get(api.Vehicles).param(Params.nextAtStop, "213"))

                    .andDo(print())
                    .andExpect(jsonPath("$.errors").isEmpty())
                    .andExpect(jsonPath("$.vehicles", hasSize(3)))
                    .andExpect(jsonPath("$.vehicles.[0].line.name").value("first"))
                    .andExpect(jsonPath("$.vehicles.[1].line.name").value("second"))
                    .andExpect(jsonPath("$.vehicles.[2].line.name").value("third"));
        }

        @Test
        @DisplayName("Returns next vehicle with details arriving at stop")
        void vehicles() throws Exception {
            when(vehicleJpaRepository.existsByLineStopTimesStopId(any()))
                    .thenReturn(true);
            when(vehicleJpaRepository.findNextAtStop(any()))
                    .thenReturn(List.of(
                            Populated.nextVehicle()
                                    .vehicleId(11L)
                                    .lineId(22L)
                                    .lineName("G3")
                                    .delay(3)
                                    .time(LocalTime.of(12, 1, 1))
                                    .build()));

            mockMvc.perform(
                    get(api.Vehicles).param(Params.nextAtStop, "213"))

                    .andExpect(jsonPath("$.errors").isEmpty())
                    .andExpect(jsonPath("$.vehicles.[0].line.stops").isEmpty())
                    .andExpect(jsonPath("$.vehicles.[0].id").value(11))
                    .andExpect(jsonPath("$.vehicles.[0].line.id").value(22))
                    .andExpect(jsonPath("$.vehicles.[0].line.name").value("G3"));
        }
    }
}