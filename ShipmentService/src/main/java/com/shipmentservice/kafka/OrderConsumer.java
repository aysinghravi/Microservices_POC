package com.shipmentservice.kafka;
import com.orderservice.model.OrderModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderConsumer.class);

    @KafkaListener(
            topics = "${spring.kafka.topic.name}"
            , groupId = "${spring.kafka.consumer.group-id}")
    public static void consume(OrderModel order){
        LOGGER.info(String.format("Order details reached shipment => %s", order.toString()));


    }
}
