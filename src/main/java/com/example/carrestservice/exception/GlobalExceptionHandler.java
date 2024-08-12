package com.example.carrestservice.exception;


import com.example.carrestservice.entity.Category;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CarException.class)
    public ResponseEntity<ApiError> handleCarException(CarException e) {
        ApiError apiError = new ApiError(HttpStatus.CONFLICT.value(), e.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CarModelNameException.class)
    public ResponseEntity<ApiError> handleCarModelNameException(CarModelNameException e) {
        ApiError apiError = new ApiError(HttpStatus.CONFLICT.value(), e.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CarModelNotFoundException.class)
    public ResponseEntity<ApiError> handleCarModelNotFoundException(CarModelNotFoundException e) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(CarNotFoundException.class)
    public ResponseEntity<ApiError> handleCarNotFoundException(CarNotFoundException e) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CategoryNameException.class)
    public ResponseEntity<ApiError> handleCategoryNameException(CategoryNameException e) {
        ApiError apiError = new ApiError(HttpStatus.CONFLICT.value(), e.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ApiError> handleCategoryNotFoundException(CategoryNotFoundException e) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ManufacturerNameException.class)
    public ResponseEntity<ApiError> handleManufacturerNameException(ManufacturerNameException e) {
        ApiError apiError = new ApiError(HttpStatus.CONFLICT.value(), e.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ManufacturerNotFoundException.class)
    public ResponseEntity<ApiError> handleManufacturerNotFoundException(ManufacturerNotFoundException e) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException e) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

}
