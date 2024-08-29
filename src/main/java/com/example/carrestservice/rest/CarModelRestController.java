package com.example.carrestservice.rest;

import com.example.carrestservice.entity.CarModel;
import com.example.carrestservice.service.CarModelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "car model" , description = "APIs for managing car models, including create, update, delete, and retrieve operations.")
@RestController
public class CarModelRestController {

    private CarModelService carModelService;

    public CarModelRestController(CarModelService carModelService) {
        this.carModelService = carModelService;
    }

    @Operation(summary = "create a new car model", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "car model was created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CarModel.class)) }),
            @ApiResponse(responseCode = "401", description = "car model can not be null", content = @Content),
            @ApiResponse(responseCode = "409", description = "car model with this name already exist", content = @Content)
    })
    @PostMapping("/car-models")
    @ResponseStatus(HttpStatus.CREATED)
    public CarModel createCarModel(@Parameter(description = "car model to be created") @RequestBody CarModel carModel) {
        return carModelService.createCarModel(carModel);
    }

    @Operation(summary = "update existing car model", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "car model was updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CarModel.class))}),
            @ApiResponse(responseCode = "401", description = "car model can not be null", content = @Content),
            @ApiResponse(responseCode = "409", description = "car model with this name already exist", content = @Content)
    })
    @PutMapping("/car-models")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCarModel(@Parameter(description = "car model to be updated") @RequestBody CarModel carModel) {
        carModelService.updateCarModel(carModel);
    }


    @Operation(summary = "remove car model by id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "car model was removed",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = CarModel.class))}),
            @ApiResponse(responseCode = "404", description = "car model with this id not found", content = @Content)
    })
    @DeleteMapping("/car-models/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCarModelById(@Parameter(description = "car model id to be removed") @PathVariable("id") Long carModelId) {
        carModelService.removeById(carModelId);
    }


    @Operation(summary = "get all car models")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = CarModel.class))}),
            @ApiResponse(responseCode = "401", description = "offset must be a non-negative integer", content = @Content),
            @ApiResponse(responseCode = "401", description = "page size must be a positive integer", content = @Content),
    })
    @GetMapping("/car-models")
    @ResponseStatus(HttpStatus.OK)
    public Page<CarModel> getCarModels(
            @Parameter(description = "sort field")
            @RequestParam(required = false, defaultValue = "carModelId") String sortField,
            @Parameter(description = "sort direction")
            @RequestParam(required = false, defaultValue = "ASC") String sortDirection,
            @Parameter(description = "offset")
            @RequestParam(required = false, defaultValue = "0") int offset,
            @Parameter(description = "page size")
            @RequestParam(required = false, defaultValue = "10") int pageSize){


        Sort.Direction direction = Sort.Direction.fromString(sortDirection);

        Sort sort = Sort.by(direction, sortField);

        Pageable pageable = PageRequest.of(offset, pageSize,sort);

        return carModelService.getAll(pageable);
    }


    @Operation(summary = "get car model by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "car model was found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CarModel.class))}),
            @ApiResponse(responseCode = "404", description = "car model with this id not found", content = @Content)
    })
    @GetMapping("/car-models/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CarModel getCarModelById(@Parameter(description = "car model id to be searched") @PathVariable Long id) {
        return carModelService.getById(id);
    }


}
