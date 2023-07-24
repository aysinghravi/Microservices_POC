package com.reservationservice.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ReservationExistsException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public ReservationExistsException(String message) {
        super(message);
    }
}
