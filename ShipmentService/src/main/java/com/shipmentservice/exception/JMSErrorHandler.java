package com.shipmentservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ErrorHandler;

@Service
@Slf4j
public class JMSErrorHandler implements ErrorHandler {
    @Override
    public void handleError(Throwable t) {
        //handle exception here
        log.info("Throwable exception {}", t);
    }
}
