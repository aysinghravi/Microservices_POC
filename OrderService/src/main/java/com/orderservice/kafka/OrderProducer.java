package com.orderservice.kafka;

import com.orderservice.model.OrderModel;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {


    private static final Logger LOGGER = LoggerFactory.getLogger(OrderProducer.class);
    private NewTopic newTopic;


    private KafkaTemplate<String, OrderModel> orderEventKafkaTemplate;

    public OrderProducer(NewTopic newTopic, KafkaTemplate<String, OrderModel> orderEventKafkaTemplate) {
        this.newTopic = newTopic;
        this.orderEventKafkaTemplate = orderEventKafkaTemplate;
    }

    public void sendMessage(OrderModel orderModel){
        LOGGER.info(String.format("Order Event => %s",orderModel.toString()));
        Message<OrderModel> message = MessageBuilder
                .withPayload(orderModel)
                .setHeader(KafkaHeaders.TOPIC, newTopic.name())
                .build();
        orderEventKafkaTemplate.send(message);
    }

}
