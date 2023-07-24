package com.returnservice.kafkaconsumer;


import com.shipmentservice.model.ShipmentModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ShipmentConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShipmentConsumer.class);

    @KafkaListener(topics = "${spring.kafka.topic.name}"
            , groupId = "${spring.kafka.consumer.group-id}")
    public static void consume(ShipmentModel shipmentModel) {
        LOGGER.info(String.format("Shipment Details Reached : %s", shipmentModel.toString()));
    }
}




