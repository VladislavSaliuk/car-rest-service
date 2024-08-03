package com.example.carrestservice.exception;

public class ManufacturerNotFoundException extends RuntimeException {

    public ManufacturerNotFoundException() {
    }

    public ManufacturerNotFoundException(String message) {
        super(message);
    }
}
