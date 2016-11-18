package com.goeuro.service.exception;

public class InvalidStationIdException extends IllegalArgumentException {
    public InvalidStationIdException(String message) {
        super(message);
    }
}
