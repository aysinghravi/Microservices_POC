package com.shipmentservice.service;

import com.shipmentservice.model.ShipmentModel;

import java.util.List;

public interface ShipmentService {
    ShipmentModel saveFlipkartShipment(ShipmentModel shipmentModel);
    ShipmentModel saveMyntraShipment(ShipmentModel shipmentModel);
    List<ShipmentModel> getAllShipment();
    ShipmentModel getShipmentById(String shipmentNumber);
    ShipmentModel updateShipment(ShipmentModel shipmentModel,String shipmentNumber);


}
