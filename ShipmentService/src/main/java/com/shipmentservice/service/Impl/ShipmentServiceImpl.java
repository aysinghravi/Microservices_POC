package com.shipmentservice.service.Impl;

import com.orderservice.model.OrderModel;
import com.shipmentservice.exception.ResourceNotFoundException;
import com.shipmentservice.model.ShipmentModel;
import com.shipmentservice.repository.ShipmentRepository;
import com.shipmentservice.service.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipmentServiceImpl implements ShipmentService {


    @Autowired
    private ShipmentRepository shipmentRepository;


    @Override
    public ShipmentModel saveMyntraShipment(ShipmentModel shipmentModel) {
        shipmentModel.setSource("Myntra");
        shipmentModel.setStatus("Shipped");
        return shipmentRepository.save(shipmentModel);
    }
    @Override
    public ShipmentModel saveFlipkartShipment(ShipmentModel shipmentModel) {
        shipmentModel.setSource("Flipkart");
        shipmentModel.setStatus("Shipped");
        return shipmentRepository.save(shipmentModel);
    }

    @Override
    public List<ShipmentModel> getAllShipment() {
        return shipmentRepository.findAll();
    }

    @Override
    public ShipmentModel getShipmentById(String shipmentNumber) {
        return shipmentRepository.findById(shipmentNumber).orElseThrow(()-> new ResourceNotFoundException("Shipment number not found "+shipmentNumber));
    }
    @Override
    public ShipmentModel updateShipment(ShipmentModel shipmentModel, String shipmentNumber) {
        ShipmentModel existingShipment = getShipmentById(shipmentNumber);
        existingShipment.setCustName(shipmentModel.getCustName() != null ? shipmentModel.getCustName() : existingShipment
                .getCustName());
        existingShipment.setAddress(shipmentModel.getAddress() != null ? shipmentModel.getAddress() : existingShipment
                .getAddress());
        existingShipment.setCity(shipmentModel.getCity() != null ? shipmentModel.getCity() : existingShipment
                .getCity());
        existingShipment.setLocality(shipmentModel.getLocality() != null ? shipmentModel.getLocality() : existingShipment
                .getLocality());
        existingShipment.setState(shipmentModel.getState() != null ? shipmentModel.getState() : existingShipment
                .getState());
        existingShipment.setZipcode(shipmentModel.getZipcode() != null ? shipmentModel.getZipcode() : existingShipment
                .getZipcode());
        existingShipment.setCountry(shipmentModel.getCountry() != null ? shipmentModel.getCountry() : existingShipment
                .getCountry());
        existingShipment.setMobile(shipmentModel.getMobile() != null ? shipmentModel.getMobile() : existingShipment
                .getMobile());
        existingShipment.setShipmentLineEntries(shipmentModel.getShipmentLineEntries() != null ? shipmentModel.getShipmentLineEntries() : existingShipment
                .getShipmentLineEntries());
        return shipmentRepository.save(existingShipment);
    }

}
