package com.shipmentservice.repository;

import com.shipmentservice.model.ShipmentModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ShipmentRepository extends MongoRepository<ShipmentModel, String> {


}
