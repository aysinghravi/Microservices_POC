package com.reservationservice.service;

import com.reservationservice.model.ReservationModel;

import java.util.List;

public interface ReservationService {

    ReservationModel createReservation(ReservationModel reservationModel);
    ReservationModel getReservationByOrderNumber(String orderNumber);
    List<ReservationModel> getAllReservations();
    ReservationModel updateReservationByOrderNumber(ReservationModel reservationModel,String orderNumber);
    void deleteReservation(String orderNumber);
}
