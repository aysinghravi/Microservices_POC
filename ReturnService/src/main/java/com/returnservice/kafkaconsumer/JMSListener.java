package com.returnservice.kafkaconsumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@EnableJms
public class JMSListener {
    @JmsListener(destination = "${jmsmq.listener.queue.name}", containerFactory = "mqQueueConnectionFactory")
    public void messageListener(String payload) {
        System.out.print("payload recieved" + payload);
        log.debug("Return Recieved  " + payload);
    }
}
