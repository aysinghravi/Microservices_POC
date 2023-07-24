package com.returnservice.service.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.returnservice.exception.ResourceNotFoundException;
import com.returnservice.kafkaconsumer.JmsProducer;
import com.returnservice.model.ReturnModel;
import com.returnservice.repository.ReturnRepository;
import com.returnservice.service.ReturnService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static com.ctc.wstx.shaded.msv_core.datatype.xsd.NumberType.save;

@Service
@Slf4j
public class ReturnServiceImpl implements ReturnService {

    @Autowired
    private ReturnRepository returnRepository;
    @Autowired
    private JmsProducer jmsProducer;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public ReturnModel saveMyntraReturn(ReturnModel returnModel) {
        String returnOrderNumber = generateOrderNumberForMyntra();
        returnModel.setReturnOrderNumber(returnOrderNumber);
        returnModel.setSource("Myntra");
        returnModel.setStatus("RETURNED");
        returnModel.setReturnON(new Date());
        String payload=convertToString(returnModel);
        // convert ReturnOrder to JMS message
        jmsProducer.sendMessage("returnQueue",payload);
//        jmsTemplate.convertAndSend("returnQueue", payload);

        return returnRepository.save(returnModel);
    }

    @Override
    public ReturnModel saveFlipkartReturn(ReturnModel returnModel) {
        String returnOrderNumber = generateOrderNumberForFlipkart();
        returnModel.setReturnOrderNumber(returnOrderNumber);
        returnModel.setSource("Flipkart");
        returnModel.setStatus("RETURNED");
        returnModel.setReturnON(new Date());
        String payload=convertToString(returnModel);
        // convert ReturnOrder to JMS message
        jmsProducer.sendMessage("returnQueue",payload);
//        jms.convertAndSend("returnQueue", payload);

        return returnRepository.save(returnModel);
    }

    private String convertToString(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.error(" Error converting Object to String" + e.getMessage());
            return "";
        }
    }

    private String generateOrderNumberForMyntra() {
        int count = (int) returnRepository.count();
        return "RETMYN" + String.format("%03d", count + 1);
    }

    private String generateOrderNumberForFlipkart() {
        int count = (int) returnRepository.count();
        return "RETFLP" + String.format("%03d", count + 1);
    }

    @Override
    public List<ReturnModel> getAllReturnOrder() {
        return returnRepository.findAll();
    }

    @Override
    public ReturnModel getReturnOrderById(String returnOrderNumber) {
        return returnRepository.findById(returnOrderNumber).orElseThrow(() -> new ResourceNotFoundException("Return order number not found" + returnOrderNumber));
    }


    public ReturnModel updateReturn(ReturnModel returnModel, String returnOrderNumber) {
        //get existing doc from db
        //populate new value from request to existing object
        ReturnModel existingReturn = returnRepository.findById(returnModel.getReturnOrderNumber()).get();
        existingReturn.setCountry(returnModel.getCountry());
        existingReturn.setState(returnModel.getState());
        existingReturn.setCity(returnModel.getCity());
        existingReturn.setLocality(returnModel.getLocality());
        existingReturn.setZipcode(returnModel.getZipcode());
        return returnRepository.save(existingReturn);
    }

}
