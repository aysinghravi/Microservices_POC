package com.reservationservice.controller;

import com.reservationservice.model.ReservationModel;
import com.reservationservice.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;
    @PostMapping("/")
    public ResponseEntity<ReservationModel> createReservation(@Valid @RequestBody ReservationModel reservationModel){
        ReservationModel createdReservation = reservationService.createReservation(reservationModel);
        return new ResponseEntity<ReservationModel>(createdReservation, HttpStatus.CREATED);
    }

    @GetMapping("/{orderNumber}")
    public ResponseEntity<ReservationModel> getReservationByOrderNumber(@PathVariable String orderNumber) {
        ReservationModel reservation = reservationService.getReservationByOrderNumber(orderNumber);
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

    @GetMapping("/allReservations")
    public List<ReservationModel> getAllReservations(){
        return reservationService.getAllReservations();
    }

    @PutMapping("/updateReservation/{orderNumber}")
    public ResponseEntity<ReservationModel> updateReservation(@RequestBody ReservationModel reservationModel,@PathVariable("orderNumber") String orderNumber){
        ReservationModel updateReservation = reservationService.updateReservationByOrderNumber(reservationModel,orderNumber);
        return new ResponseEntity<ReservationModel>(updateReservation,HttpStatus.OK);
    }

    @DeleteMapping("/deleteReservation/{orderNumber}")
    public ResponseEntity<?> deleteReservation(@PathVariable("orderNumber") String orderNumber){
        reservationService.deleteReservation(orderNumber);
        return new ResponseEntity<>("Reservation deleted Successfully",HttpStatus.NO_CONTENT);
    }
}
