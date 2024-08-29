package com.example.carrestservice.rest;

import com.example.carrestservice.entity.CarModel;
import com.example.carrestservice.entity.Manufacturer;
import com.example.carrestservice.service.ManufacturerService;
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


@Tag(name = "manufacturer" , description = "APIs for managing manufacturers, including create, update, delete, and retrieve operations.")
@RestController
public class ManufacturerRestController {
    private ManufacturerService manufacturerService;

    public ManufacturerRestController(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }

    @Operation(summary = "create a new manufacturer", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "manufacturer was created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Manufacturer.class)) }),
            @ApiResponse(responseCode = "401", description = "manufacturer can not be null", content = @Content),
            @ApiResponse(responseCode = "409", description = "manufacturer with this name already exist", content = @Content)
    })
    @PostMapping("/manufacturers")
    @ResponseStatus(HttpStatus.CREATED)
    public Manufacturer createManufacturer(@Parameter(description = "manufacturer to be created") @RequestBody Manufacturer manufacturer) {
        return manufacturerService.createManufacturer(manufacturer);
    }

    @Operation(summary = "update existing manufacturer", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "manufacturer was updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Manufacturer.class))}),
            @ApiResponse(responseCode = "401", description = "manufacturer can not be null", content = @Content),
            @ApiResponse(responseCode = "409", description = "manufacturer with this name already exist", content = @Content)
    })
    @PutMapping("/manufacturers")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateManufacturer(@Parameter(description = "manufacturer to be updated") @RequestBody Manufacturer manufacturer) {
        manufacturerService.updateManufacturer(manufacturer);
    }


    @Operation(summary = "remove manufacturer by id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "manufacturer was removed",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Manufacturer.class))}),
            @ApiResponse(responseCode = "404", description = "manufacturer with this id not found", content = @Content)
    })
    @DeleteMapping("manufacturers/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeManufacturerById(@Parameter(description = "manufacturer id to be removed") @PathVariable("id") Long manufacturerId) {
        manufacturerService.removeById(manufacturerId);
    }


    @Operation(summary = "get all manufacturers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Manufacturer.class))}),
            @ApiResponse(responseCode = "401", description = "offset must be a non-negative integer", content = @Content),
            @ApiResponse(responseCode = "401", description = "page size must be a positive integer", content = @Content),
    })
    @GetMapping("/manufacturers")
    @ResponseStatus(HttpStatus.OK)
    public Page<Manufacturer> getManufacturers(
            @Parameter(description = "sort field")
            @RequestParam(required = false, defaultValue = "manufacturerId") String sortField,
            @Parameter(description = "sort direction")
            @RequestParam(required = false, defaultValue = "ASC") String sortDirection,
            @Parameter(description = "offset")
            @RequestParam(required = false, defaultValue = "0") int offset,
            @Parameter(description = "page size")
            @RequestParam(required = false, defaultValue = "10") int pageSize){

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);

        Sort sort = Sort.by(direction, sortField);

        Pageable pageable = PageRequest.of(offset, pageSize,sort);

        return manufacturerService.getAll(pageable);
    }



    @Operation(summary = "get manufacturer by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "manufacturer was found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CarModel.class))}),
            @ApiResponse(responseCode = "404", description = "manufacturer with this id not found", content = @Content)
    })
    @GetMapping("/manufacturers/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Manufacturer getManufacturerById(@Parameter(description = "manufacturer id to be searched") @PathVariable Long id) {
        return manufacturerService.getById(id);
    }


}
