package com.shipmentservice.kafka;



import com.shipmentservice.model.ShipmentModel;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class ShipmentProducer {


    private static final Logger LOGGER = LoggerFactory.getLogger(ShipmentProducer.class);
    private NewTopic newTopic;


    private KafkaTemplate<String, ShipmentModel> shipmentModelKafkaTemplate;

    public ShipmentProducer(NewTopic newTopic, KafkaTemplate<String, ShipmentModel> shipmentModelKafkaTemplate) {
        this.newTopic = newTopic;
        this.shipmentModelKafkaTemplate = shipmentModelKafkaTemplate;
    }

    public void sendMessage(ShipmentModel shipmentModel){
        LOGGER.info(String.format("Shipment Event => %s",shipmentModel.toString()));
        Message<ShipmentModel> message = MessageBuilder
                .withPayload(shipmentModel)
                .setHeader(KafkaHeaders.TOPIC, newTopic.name())
                .build();
        shipmentModelKafkaTemplate.send(message);
    }
}
