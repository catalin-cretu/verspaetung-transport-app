package com.github.catalin.cretu.verspaetung.web.vehicle;

import com.github.catalin.cretu.verspaetung.ErrorResult;
import com.github.catalin.cretu.verspaetung.Result;
import com.github.catalin.cretu.verspaetung.api.vehicle.Stop;
import com.github.catalin.cretu.verspaetung.api.vehicle.Vehicle;
import com.github.catalin.cretu.verspaetung.api.vehicle.VehicleService;
import com.github.catalin.cretu.verspaetung.web.ApiResponse;
import com.github.catalin.cretu.verspaetung.web.ErrorView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.github.catalin.cretu.verspaetung.web.Paths.Params;
import static com.github.catalin.cretu.verspaetung.web.Paths.api;
import static java.util.stream.Collectors.toList;

@Slf4j
@RestController
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(final VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping(path = api.Vehicles)
    public ApiResponse<VehiclesResponse> findAll(@RequestParam Map<String, String> allParams) {
        var vehiclesResult = findVehicles(allParams);
        if (vehiclesResult.hasErrors()) {
            return ApiResponse.badRequest(VehiclesResponse.builder()
                    .errors(toErrorViews(vehiclesResult.getErrors()))
                    .build());
        }
        return ApiResponse.ok(VehiclesResponse.builder()
                .vehicles(toVehicleViews(vehiclesResult.get()))
                .build());
    }

    private static List<ErrorView> toErrorViews(final Set<ErrorResult> errors) {
        return errors.stream()
                .map(VehicleController::toErrorView)
                .collect(toList());
    }

    private static ErrorView toErrorView(final ErrorResult errorResult) {
        return ErrorView.builder()
                .code(errorResult.getCode())
                .message(errorResult.getMessage())
                .build();
    }

    private Result<List<Vehicle>> findVehicles(final Map<String, String> allParams) {
        if (allParams.containsKey(Params.lineName)) {
            var lineName = allParams.get(Params.lineName);

            return vehicleService.findByLineName(lineName);
        } else if (allParams.containsKey(Params.nextAtStop)) {
            var stopId = allParams.get(Params.nextAtStop);

            if (isNotNumeric(stopId)) {
                return Result.error(Params.nextAtStop, "Parameter must be a numeric stop ID");
            }
            return vehicleService.findNextAtStop(Long.valueOf(stopId));
        }
        return Result.ok(vehicleService.findAllVehicles());
    }

    private static boolean isNotNumeric(final String stopIdParam) {
        try {
            Long.valueOf(stopIdParam);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    private static List<VehicleView> toVehicleViews(final List<Vehicle> vehicles) {
        return vehicles.stream()
                .map(VehicleController::toVehicleView)
                .collect(toList());
    }

    private static VehicleView toVehicleView(final Vehicle vehicle) {
        var line = vehicle.getLine();

        return VehicleView.builder()
                .id(vehicle.getId())
                .line(LineView.builder()
                        .id(line.getId())
                        .name(line.getName())
                        .delay(line.getDelay())
                        .stops(toStopViews(line.getStops()))
                        .build())
                .build();
    }

    private static List<StopView> toStopViews(final List<Stop> stops) {
        return stops.stream()
                .map(stop -> StopView.builder()
                        .id(stop.getId())
                        .time(stop.getTime())
                        .coordinates(new CoordinatesView(stop.getXCoordinate(), stop.getYCoordinate()))
                        .build())
                .collect(toList());
    }
}
