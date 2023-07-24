package com.orderservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderservice.exception.ResourceNotFoundException;
import com.orderservice.kafka.OrderProducer;
import com.orderservice.model.ErrorObject;
import com.orderservice.model.OrderModel;
import com.orderservice.service.OrderService;
import com.reservationservice.exception.ReservationNotFoundException;
import com.reservationservice.model.ReservationModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(
        name = "CRUD REST APIs for microservice",
        description = "CRUD REST APIs - Create Orders, Update Orders, Get Orders, Get All Orders"
)
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderProducer orderProducer;

    @PreAuthorize("hasAuthority('Myntra User')")
    @Operation(
            summary = "Create Myntra Order REST API",
            description = "Create Myntra Order REST API is used to save order in a database"
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP Status 201 CREATED"
    )
    @PostMapping(value = "/createOrder/myntra")
    public ResponseEntity<OrderModel> saveOrderMyntra(@Valid @RequestBody OrderModel orderModel) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        String reservationApiUrl = "http://localhost:9096/api/reservations/" + orderModel.getOrderNumber();
        HttpRequest reservationRequest = HttpRequest.newBuilder()
                .uri(URI.create(reservationApiUrl))
                .build();
        HttpResponse<InputStream> reservationResponse = httpClient.send(reservationRequest, HttpResponse.BodyHandlers.ofInputStream());
        if (reservationResponse.statusCode() == HttpStatus.OK.value()) {
            ObjectMapper objectMapper = new ObjectMapper();
            ReservationModel reservation = objectMapper.readValue(reservationResponse.body(), ReservationModel.class);

            if ("Reservation Successful".equals(reservation.getStatus())) {
                // Reservation exists, order can be created
                OrderModel savedOrder = orderService.saveOrderMyntra(orderModel);
                orderProducer.sendMessage(savedOrder);
                return new ResponseEntity<OrderModel>(savedOrder, HttpStatus.CREATED);
            } else {
                // Reservation does not exist, order cannot be created
                throw new ReservationNotFoundException("Reservation not found with the order number: " + orderModel.getOrderNumber());
            }
        } else {
            // Reservation not found
            throw new ReservationNotFoundException("Reservation not found with the order number: " + orderModel.getOrderNumber());
        }

    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorObject> handleAccessDeniedException(Exception ex, WebRequest request) {
        ErrorObject errorObject = new ErrorObject();

        errorObject.setStatusCode(HttpStatus.FORBIDDEN.value());

        errorObject.setMessage(ex.getMessage());

        errorObject.setTimestamp(new Date());

        return new ResponseEntity<ErrorObject>(errorObject, HttpStatus.FORBIDDEN);
    }

    @PostMapping(value = "/createOrder/flipkart", consumes = {MediaType.APPLICATION_XML_VALUE}, produces = {
            MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasAuthority('Flipkart User')")
    @Operation(
            summary = "Create Flipkart Order REST API",
            description = "Create Flipkart Order REST API is used to save order in a database"
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP Status 201 CREATED"
    )
    public ResponseEntity<OrderModel> saveOrderFlipKart(@Valid @RequestBody OrderModel orderModel) {
        RestTemplate restTemplate = new RestTemplate();
        String reservationApiUrl = "http://localhost:9096/api/reservations/" + orderModel.getOrderNumber();
        ResponseEntity<ReservationModel> response = restTemplate.getForEntity(reservationApiUrl, ReservationModel.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new ResourceNotFoundException("Reservation not found with the order number: " + orderModel.getOrderNumber());
        }
        OrderModel savedOrder = orderService.saveOrderFlipkart(orderModel);
        orderProducer.sendMessage(savedOrder);
        return new ResponseEntity<OrderModel>(savedOrder, HttpStatus.CREATED);
    }

    @GetMapping("/getAllOrders")
    @PreAuthorize("hasAuthority('Admin')")
    @Operation(
            summary = "Get All Orders REST API",
            description = "Get All Orders REST API is used to get a all the orders from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 SUCCESS"
    )
    public List<OrderModel> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/OrderById/{orderNumber}")
    @PreAuthorize("hasAuthority('Admin')")
    @Operation(
            summary = "Get Order By ID REST API",
            description = "Get Order By ID REST API is used to get a single order from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 SUCCESS"
    )
    public ResponseEntity<OrderModel> getOrderById(@PathVariable("orderNumber") String orderNumber) {
        return new ResponseEntity<OrderModel>(orderService.getOrderById(orderNumber), HttpStatus.OK);
    }

    @PutMapping("/updateOrder/{orderNumber}")
    @PreAuthorize("hasAuthority('Admin')")
    @Operation(
            summary = "Update Order REST API",
            description = "Update Order REST API is used to update a particular order in the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 SUCCESS"
    )
    public ResponseEntity<OrderModel> updateOrder(@RequestBody OrderModel orderModel, @PathVariable("orderNumber") String orderNumber) {
        OrderModel updateOrder = orderService.updateOrder(orderModel, orderNumber);
        orderProducer.sendMessage(orderService.updateOrder(orderModel, orderNumber));
        return new ResponseEntity<OrderModel>(updateOrder, HttpStatus.OK);
    }

    @PutMapping("/deleteOrder/{orderNumber}")
    @PreAuthorize("hasAuthority('Admin')")
    @Operation(
            summary = "Delete Order REST API",
            description = "Delete Order REST API is used to delete a particular order in the database"
    )
    @ApiResponse(
            responseCode = "204",
            description = "HTTP Status 204 SUCCESS"
    )
    public ResponseEntity<?> deleteOrder(@PathVariable("orderNumber") String orderNumber){
        orderService.deleteOrder(orderNumber);
        return new ResponseEntity<>("Order deleted Successfully",HttpStatus.NO_CONTENT);
    }
}
