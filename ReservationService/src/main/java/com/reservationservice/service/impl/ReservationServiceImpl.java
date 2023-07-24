package com.reservationservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reservationservice.exception.ReservationExistsException;
import com.reservationservice.exception.ReservationFailedException;
import com.reservationservice.exception.ReservationNotFoundException;
import com.reservationservice.model.ReservationModel;
import com.reservationservice.repository.ReservationRepository;
import com.reservationservice.service.ReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ReservationServiceImpl implements ReservationService {

//    @Autowired
//    private JmsTemplate jmsTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public ReservationModel createReservation(ReservationModel reservationModel) {
        ReservationModel existingReservation = reservationRepository.findByOrderNumber(reservationModel.getOrderNumber());
        if(existingReservation!=null){
            String orderNumber = existingReservation.getOrderNumber();
            throw new ReservationExistsException("Reservation exists with the order number: "+orderNumber);
        }
        try {
            String reservationId = generateReservationNumberForOrder();
            reservationModel.setReservationId(reservationId);
            reservationModel.setCreateAt(new Date());
            reservationModel.setStatus("Reservation Successful");
            String ReserveModelToOrderString=convertToString(reservationModel);
            // convert ReserveModel to JMS message
//            jmsTemplate.convertAndSend("ReserveQueue", ReserveModelToOrderString);
            return reservationRepository.save(reservationModel);
        }catch (ReservationFailedException e){
            reservationModel.setStatus("Reservation Failed");
            throw new ReservationFailedException("Reservation failed");
        }
    }
    private String convertToString(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.error(" Error converting Object to String" + e.getMessage());
            return "";
        }
    }

    private String generateReservationNumberForOrder() {
        int count = (int) reservationRepository.count();
        return "RES" + String.format("%03d", count + 1);
    }

    @Override
    public ReservationModel getReservationByOrderNumber(String orderNumber) {
        Optional<ReservationModel> reservationModel = Optional.ofNullable(reservationRepository.findByOrderNumber(orderNumber));
        if (reservationModel.isPresent()) {
            return reservationModel.get();
        }
        throw new ReservationNotFoundException("Reservation not found with the order number: " + orderNumber);
    }

    @Override
    public List<ReservationModel> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public ReservationModel updateReservationByOrderNumber(ReservationModel reservationModel, String orderNumber) {
        ReservationModel existingReservation = getReservationByOrderNumber(orderNumber);
        existingReservation.setItemList(reservationModel.getItemList() != null ? reservationModel.getItemList() : existingReservation.getItemList());
        existingReservation.setStatus(reservationModel.getStatus() != null ? reservationModel.getStatus() : existingReservation.getStatus());
        return reservationRepository.save(existingReservation);
    }

    @Override
    public void deleteReservation(String orderNumber) {
        ReservationModel reservationModel = getReservationByOrderNumber(orderNumber);
        reservationRepository.delete(reservationModel);
    }
}
