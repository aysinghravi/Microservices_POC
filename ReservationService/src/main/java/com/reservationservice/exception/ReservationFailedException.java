package com.reservationservice.exception;

public class ReservationFailedException extends RuntimeException {
    public ReservationFailedException(String message) {
        super(message);
    }
}
