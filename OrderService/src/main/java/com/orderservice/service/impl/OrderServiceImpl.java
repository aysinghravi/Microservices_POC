package com.orderservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderservice.exception.OrderExistsException;
import com.orderservice.exception.ResourceNotFoundException;
import com.orderservice.model.OrderModel;
import com.orderservice.repository.OrderRepository;
import com.orderservice.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ObjectMapper objectMapper;
//    @Autowired
//    private JmsTemplate jmsTemplate;

    @Override
    public OrderModel saveOrderMyntra(OrderModel orderModel) {
        if (!orderModel.getOrderNumber().startsWith("MYN")) {
            throw new IllegalArgumentException("Order number should start with MYN");
        }
        OrderModel existingOrder = orderRepository.findByOrderNumber(orderModel.getOrderNumber());
        if (existingOrder != null) {
            String orderNumber = existingOrder.getOrderNumber();
            throw new OrderExistsException("Order Exists with the orderNumber: " + orderNumber);
        }
        orderModel.setStatus("CREATED");
        orderModel.setSource("MYNTRA");
        orderModel.setCreatedAt(new Date());
        String OrderModelToString=convertToString(orderModel);
        // convert OrderModel to JMS message
//        jmsTemplate.convertAndSend("orderQueue", OrderModelToString);
        return orderRepository.save(orderModel);
    }

    private String generateOrderNumberForMyntra() {
        int count = (int) orderRepository.count();
        return "MYN" + String.format("%03d", count + 1);
    }

    private String generateOrderNumberForFlipkart() {
        int count = (int) orderRepository.count();
        return "FLP" + String.format("%03d", count + 1);
    }
    private String convertToString(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.error(" Error converting Object to String" + e.getMessage());
            return "";
        }
    }

//    private boolean isOrderNumberReserved(String orderNumber) {
//        List<String> reservedOrderNumbers = Arrays.asList("FLP001", "FLP002", "FLP003", "MYN001", "MYN002", "MYN003"); // Add reserved order numbers here
//        return reservedOrderNumbers.contains(orderNumber);
//    }

    @Override
    public OrderModel saveOrderFlipkart(OrderModel orderModel) {
        if (!orderModel.getOrderNumber().startsWith("FLP")) {
            throw new IllegalArgumentException("Order number should start with FLP");
        }
        OrderModel existingOrder = orderRepository.findByOrderNumber(orderModel.getOrderNumber());
        if (existingOrder != null) {
            String orderNumber = existingOrder.getOrderNumber();
            throw new OrderExistsException("Order Exists with the orderNumber: " + orderNumber);
        }
//        boolean isOrderNumberReserved = isOrderNumberReserved(orderModel.getOrderNumber());
//        if (!isOrderNumberReserved) {
//            String orderNumber = generateOrderNumberForFlipkart();
//            orderModel.setOrderNumber(orderNumber);
//        }
        String orderNumber = generateOrderNumberForMyntra();
        orderModel.setOrderNumber(orderNumber);
        orderModel.setStatus("CREATED");
        orderModel.setSource("FLIPKART");
        orderModel.setCreatedAt(new Date());
        String StringToOrderModel=convertToString(orderModel);
        // convert OrderModel to JMS message
//        jmsTemplate.convertAndSend("orderQueue", StringToOrderModel);
        return orderRepository.save(orderModel);
    }

    @Override
    public List<OrderModel> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public OrderModel getOrderById(String orderNumber) {
        Optional<OrderModel> orderModel = orderRepository.findById(orderNumber);
        if (orderModel.isPresent()) {
            return orderModel.get();
        }
        throw new ResourceNotFoundException("Order id is not present by the id: " + orderNumber);
    }
    @Override
    public OrderModel getOrderByOrderNumber(String orderNumber) {
        OrderModel orderModel = orderRepository.findByOrderNumber(orderNumber);
        if(orderModel!=null){
            return orderModel;
        }
        throw new ResourceNotFoundException("Order id is not present by the id: " + orderNumber);
    }

    @Override
    public OrderModel updateOrder(OrderModel orderModel, String orderNumber) {
        OrderModel existingOrder = getOrderById(orderNumber);
        existingOrder.setCustName(orderModel.getCustName() != null ? orderModel.getCustName() : existingOrder
                .getCustName());
        existingOrder.setAddress(orderModel.getAddress() != null ? orderModel.getAddress() : existingOrder
                .getAddress());
        existingOrder.setCity(orderModel.getCity() != null ? orderModel.getCity() : existingOrder
                .getCity());
        existingOrder.setLocality(orderModel.getLocality() != null ? orderModel.getLocality() : existingOrder
                .getLocality());
        existingOrder.setState(orderModel.getState() != null ? orderModel.getState() : existingOrder
                .getState());
        existingOrder.setEmail(orderModel.getEmail() != null ? orderModel.getEmail() : existingOrder
                .getEmail());
        existingOrder.setZipcode(orderModel.getZipcode() != null ? orderModel.getZipcode() : existingOrder
                .getZipcode());
        existingOrder.setCountry(orderModel.getCountry() != null ? orderModel.getCountry() : existingOrder
                .getCountry());
        existingOrder.setMobile(orderModel.getMobile() != null ? orderModel.getMobile() : existingOrder
                .getMobile());
        existingOrder.setStatus(orderModel.getStatus() != null ? orderModel.getStatus() : existingOrder
                .getStatus());
        existingOrder.setOrderLineEntries(orderModel.getOrderLineEntries() != null ? orderModel.getOrderLineEntries() : existingOrder
                .getOrderLineEntries());
        return orderRepository.save(existingOrder);
    }

    @Override
    public void deleteOrder(String orderNumber) {
        OrderModel orderModel = getOrderById(orderNumber);
        orderRepository.delete(orderModel);
    }
}
