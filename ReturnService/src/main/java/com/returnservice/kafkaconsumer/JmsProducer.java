package com.returnservice.kafkaconsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class JmsProducer {
    @Autowired
    private JmsTemplate jmsTemplate;
    public void sendMessage(String message,String s) {
        jmsTemplate.convertAndSend(message,s);
    }
}
