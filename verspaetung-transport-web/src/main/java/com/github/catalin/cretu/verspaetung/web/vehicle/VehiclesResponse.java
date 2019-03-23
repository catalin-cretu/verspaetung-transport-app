package com.github.catalin.cretu.verspaetung.web.vehicle;

import com.github.catalin.cretu.verspaetung.web.ErrorView;
import com.github.catalin.cretu.verspaetung.web.ErrorsResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Objects.requireNonNullElse;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
class VehiclesResponse extends ErrorsResponse {

    private Set<VehicleView> vehicles;

    @Builder
    public VehiclesResponse(final Set<VehicleView> vehicles, final List<ErrorView> errors) {
        super(requireNonNullElse(errors, emptyList()));

        this.vehicles = requireNonNullElse(vehicles, emptySet());
    }
}
