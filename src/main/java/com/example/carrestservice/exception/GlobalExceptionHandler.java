package com.example.carrestservice.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(CarException.class)
    public ApiError handleCarException(CarException e) {
        return new ApiError(HttpStatus.CONFLICT.value(), e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(CarModelNameException.class)
    public ApiError handleCarModelNameException(CarModelNameException e) {
        return new ApiError(HttpStatus.CONFLICT.value(), e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CarModelNotFoundException.class)
    public ApiError handleCarModelNotFoundException(CarModelNotFoundException e) {
        return new ApiError(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CarNotFoundException.class)
    public ApiError handleCarNotFoundException(CarNotFoundException e) {
        return new ApiError(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(CategoryNameException.class)
    public ApiError handleCategoryNameException(CategoryNameException e) {
        return new ApiError(HttpStatus.CONFLICT.value(), e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CategoryNotFoundException.class)
    public ApiError handleCategoryNotFoundException(CategoryNotFoundException e) {
        return new ApiError(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ManufacturerNameException.class)
    public ApiError handleManufacturerNameException(ManufacturerNameException e) {
        return new ApiError(HttpStatus.CONFLICT.value(), e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ManufacturerNotFoundException.class)
    public ApiError handleManufacturerNotFoundException(ManufacturerNotFoundException e) {
        return new ApiError(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ApiError handleIllegalArgumentException(IllegalArgumentException e) {
        return new ApiError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public ApiError handleThrowable(Throwable e) {
        return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred: " + e.getMessage());
    }

}
