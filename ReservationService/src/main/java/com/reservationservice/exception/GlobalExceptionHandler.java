package com.reservationservice.exception;

import com.reservationservice.model.ErrorObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<ErrorObject> handleReservationNotFoundException(ReservationNotFoundException ex, WebRequest request) {

        ErrorObject errorObject = new ErrorObject();

        errorObject.setStatusCode(HttpStatus.NOT_FOUND.value());

        errorObject.setMessage(ex.getMessage());

        errorObject.setTimestamp(new Date());

        return new ResponseEntity<ErrorObject>(errorObject, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ReservationFailedException.class)
    public ResponseEntity<ErrorObject> handleReservationFailedException(ReservationFailedException ex, WebRequest request) {

        ErrorObject errorObject = new ErrorObject();

        errorObject.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

        errorObject.setMessage(ex.getMessage());

        errorObject.setTimestamp(new Date());

        return new ResponseEntity<ErrorObject>(errorObject, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(ReservationExistsException.class)
    public ResponseEntity<ErrorObject> handleReservationExistsException(ReservationExistsException ex, WebRequest request) {

        ErrorObject errorObject = new ErrorObject();

        errorObject.setStatusCode(HttpStatus.CONFLICT.value());

        errorObject.setMessage(ex.getMessage());

        errorObject.setTimestamp(new Date());

        return new ResponseEntity<ErrorObject>(errorObject, HttpStatus.CONFLICT);
    }
}
