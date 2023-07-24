package com.returnservice.controller;

import com.ibm.mq.jms.MQQueue;
import com.returnservice.kafkaconsumer.returnProducer;
import com.returnservice.model.ReturnModel;
import com.returnservice.service.ReturnService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.jms.JMSException;
import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/return")
@RestController
@Tag(
        name = "CRUD REST APIs for microservice",
        description = "CRUD REST APIs - Create Return, Update return, Get Return, Get All Return"
)
public class ReturnController {

    @Autowired
    private ReturnService returnService;
    @Autowired
    private returnProducer returnproducer;
    @Autowired
    private JmsTemplate jmsTemplate;

    @PostMapping(value = "/createReturn/myntra", consumes = {MediaType.APPLICATION_XML_VALUE}, produces = {
            MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasAuthority('Myntra User')")
    @Operation(
            summary = "Create Myntra Return REST API",
            description = "Create Myntra Return REST API is used to save Return-order in a database"
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP Status 201 CREATED"
    )
    public ResponseEntity<ReturnModel> saveMyntraReturn(@Valid @RequestBody ReturnModel returnModel) {
        ReturnModel savedReturn = returnService.saveMyntraReturn(returnModel);
        returnproducer.sendMessage(savedReturn);
        //jmsTemplate.convertAndSend("QNamePMOC","test1");
        return new ResponseEntity<ReturnModel>(savedReturn, HttpStatus.CREATED);
    }


    @PostMapping("/createReturn/flipkart")
    @PreAuthorize("hasAuthority('Flipkart User')")
    @Operation(
            summary = "Create Flipkart Return REST API",
            description = "Create Flipkart Return REST API is used to save Return-order in a database"
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP Status 201 CREATED"
    )
    public ResponseEntity<ReturnModel> saveReturn(@Valid @RequestBody ReturnModel returnModel) {
        ReturnModel savedReturn = returnService.saveFlipkartReturn(returnModel);
        returnproducer.sendMessage(savedReturn);
        return new ResponseEntity<ReturnModel>(savedReturn, HttpStatus.CREATED);
    }


    @GetMapping(value = "/all")
    @PreAuthorize("hasAuthority('Admin')")
    @Operation(
            summary = "Get All Return REST API",
            description = "Get All Return REST API is used to get a all the Return-orders from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 SUCCESS"
    )
    public List<ReturnModel> getAllReturnOrder() {
        return returnService.getAllReturnOrder();
    }

    @PreAuthorize("hasAuthority('Admin')")
    @GetMapping("{returnOrderNumber}")
    @Operation(
            summary = "Get Return By ID REST API",
            description = "Get Return By ID REST API is used to get a single Return-order from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 SUCCESS"
    )
    public ResponseEntity<ReturnModel> getReturnOrderById(@PathVariable("returnOderNumber") String returnOrderNumber) {
        return new ResponseEntity<ReturnModel>(returnService.getReturnOrderById(returnOrderNumber), HttpStatus.OK);
    }

    @PutMapping("/updateReturn/{returnOrderNumber}")
    @PreAuthorize("hasAuthority('Admin')")
    @Operation(
            summary = "Update Return REST API",
            description = "Update Return REST API is used to update a particular Return-order in the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 SUCCESS"
    )
    public ResponseEntity<ReturnModel> updateReturn(@RequestBody ReturnModel returnModel, @PathVariable("returnOrderNumber") String returnOrderNumber) {
        returnproducer.sendMessage((returnService.updateReturn(returnModel, returnOrderNumber)));
        return new ResponseEntity<ReturnModel>(returnService.updateReturn(returnModel, returnOrderNumber), HttpStatus.OK);
    }


}
