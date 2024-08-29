package com.example.carrestservice.rest;

import com.example.carrestservice.entity.Car;
import com.example.carrestservice.service.CarService;
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

@RestController
@Tag(name = "car" , description = "APIs for managing cars, including create, update, delete, and retrieve operations.")
public class CarRestController {
    private CarService carService;
    public CarRestController(CarService carService) {
        this.carService = carService;
    }

    @Operation(summary = "create a new car", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "car was created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Car.class)) }),
            @ApiResponse(responseCode = "401", description = "car can not be null", content = @Content),
            @ApiResponse(responseCode = "409", description = "car with this car model id already exists", content = @Content)
    })
    @PostMapping("/cars")
    @ResponseStatus(HttpStatus.CREATED)
    public Car createCar(@Parameter(description = "car to be created") @RequestBody Car car) {
        return carService.createCar(car);
    }


    @Operation(summary = "update existing car", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "car was updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Car.class))}),
            @ApiResponse(responseCode = "401", description = "car can not be null", content = @Content),
            @ApiResponse(responseCode = "409", description = "car with this car model id already exists", content = @Content)
    })
    @PutMapping("/cars")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCar(@Parameter(description = "car to be updated") @RequestBody Car car) {
        carService.updateCar(car);
    }

    @Operation(summary = "remove car by id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "car was removed",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Car.class))}),
            @ApiResponse(responseCode = "404", description = "car with this id not found", content = @Content)
    })
    @DeleteMapping("/cars/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCarById(@Parameter(description = "car id to be removed") @PathVariable("id") Long carId) {
        carService.removeById(carId);
    }

    @Operation(summary = "get all cars")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Car.class))}),
            @ApiResponse(responseCode = "401", description = "offset must be a non-negative integer", content = @Content),
            @ApiResponse(responseCode = "401", description = "page size must be a positive integer", content = @Content),
    })
    @GetMapping("/cars")
    @ResponseStatus(HttpStatus.OK)
    public Page<Car> getCars(
            @Parameter(description = "sort field")
            @RequestParam(required = false, defaultValue = "carId") String sortField,
            @Parameter(description = "sort direction")
            @RequestParam(required = false, defaultValue = "ASC") String sortDirection,
            @Parameter(description = "offset")
            @RequestParam(required = false, defaultValue = "0") int offset,
            @Parameter(description = "page size")
            @RequestParam(required = false, defaultValue = "10") int pageSize) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);

        Sort sort = Sort.by(direction, sortField);

        Pageable pageable = PageRequest.of(offset, pageSize,sort);

        return carService.getAll(pageable);
    }

    @Operation(summary = "get car by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "car was found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Car.class))}),
            @ApiResponse(responseCode = "404", description = "car with this id not found", content = @Content)
    })
    @GetMapping("/cars/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Car getCarById(@Parameter(description = "car id to be searched") @PathVariable Long id) {
        return carService.getById(id);
    }

}
