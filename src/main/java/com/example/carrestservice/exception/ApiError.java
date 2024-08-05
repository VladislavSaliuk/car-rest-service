package com.example.carrestservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {

    private int statusCode;

    private String message;

    public ApiError(String message) {
        this.message = message;
    }
}
