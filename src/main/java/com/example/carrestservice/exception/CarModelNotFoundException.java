package com.example.carrestservice.exception;

public class CarModelNotFoundException extends RuntimeException {

    public CarModelNotFoundException() {

    }

    public CarModelNotFoundException(String message) {
        super(message);
    }


}
