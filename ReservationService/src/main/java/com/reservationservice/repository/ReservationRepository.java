package com.reservationservice.repository;

import com.reservationservice.model.ReservationModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReservationRepository extends MongoRepository<ReservationModel,String> {
    ReservationModel findByOrderNumber(String orderNumber);
}
