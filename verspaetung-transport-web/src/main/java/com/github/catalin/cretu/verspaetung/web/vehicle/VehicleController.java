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

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private static List<ErrorView> toErrorViews(final List<ErrorResult> errors) {
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
        } else if (hasStopParams(allParams)) {
            return findVehiclesByStop(allParams);
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

    private static boolean hasStopParams(final Map<String, String> allParams) {
        return allParams.containsKey(Params.time)
                || allParams.containsKey(Params.stopX)
                || allParams.containsKey(Params.stopY);
    }

    private Result<List<Vehicle>> findVehiclesByStop(final Map<String, String> allParams) {
        var errorResults = new ArrayList<ErrorResult>();

        var stopTimeResult = toStopTime(allParams.get(Params.time));
        if (stopTimeResult.hasErrors()) {
            errorResults.addAll(stopTimeResult.getErrors());
        }
        var stopXCoordResult = toCoordinate(allParams.get(Params.stopX), "X");
        if (stopXCoordResult.hasErrors()) {
            errorResults.addAll(stopXCoordResult.getErrors());
        }
        var stopYCoordResult = toCoordinate(allParams.get(Params.stopY), "Y");
        if (stopYCoordResult.hasErrors()) {
            errorResults.addAll(stopYCoordResult.getErrors());
        }
        if (errorResults.isEmpty()) {
            return vehicleService.findByStop(
                    stopTimeResult.get(), stopXCoordResult.get(), stopYCoordResult.get());
        }
        return Result.errors(errorResults);
    }

    private static Result<LocalTime> toStopTime(final String stopTimeParam) {
        try {
            if (stopTimeParam.isBlank()) {
                return Result.ok(null);
            }
            return Result.ok(LocalTime.parse(stopTimeParam));
        } catch (DateTimeParseException e) {
            return Result.error("time", "stop time parameter must be formatted as HH:mm:ss");
        }
    }

    private static Result<Integer> toCoordinate(final String coordinateParam, final String coordinateName) {
        try {
            if (coordinateParam.isBlank()) {
                return Result.ok(null);
            }
            return Result.ok(Integer.parseInt(coordinateParam));
        } catch (NumberFormatException e) {
            return Result.error(
                    "stop" + coordinateName,
                    "stop " + coordinateName + " coordinate parameter must be an integer");
        }
    }
}