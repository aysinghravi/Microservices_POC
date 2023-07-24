package com.shipmentservice.ibmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@EnableJms
public class JMSListener {
    @JmsListener(destination = "${jmsmq.listener.order.queue.name}", containerFactory = "mqQueueConnectionFactory")
    public void orderMessageListener(String payload) {
        System.out.print("payload recieved" + payload);
        log.debug("Reservation Recieved  " + payload);
    }

    @JmsListener(destination = "${jmsmq.listener.return.queue.name}", containerFactory = "mqQueueConnectionFactory")
    public void returnMessageListener(String payload) {
        System.out.print("payload recieved" + payload);
        log.debug("Reservation Recieved  " + payload);
    }
}