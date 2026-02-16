package edu.eci.arsw.blueprints.controllers.dto;

import edu.eci.arsw.blueprints.model.Point;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record NewBlueprintRequest(
        @NotBlank String author,
        @NotBlank String name,
        @Valid List<Point> points
) { }

