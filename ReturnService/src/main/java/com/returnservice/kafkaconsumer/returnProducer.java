package com.returnservice.kafkaconsumer;

import com.returnservice.model.ReturnModel;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class returnProducer {


    private static final Logger LOGGER = LoggerFactory.getLogger(returnProducer.class);
    private NewTopic newTopic;


    private KafkaTemplate<String, ReturnModel> returnModelKafkaTemplate;

    public returnProducer(NewTopic newTopic, KafkaTemplate<String, ReturnModel> returnModelKafkaTemplate) {
        this.newTopic = newTopic;
        this.returnModelKafkaTemplate = returnModelKafkaTemplate;
    }

    public void sendMessage(ReturnModel returnModel) {
        LOGGER.info(String.format("Return Event => %s", returnModel.toString()));
        Message<ReturnModel> message = MessageBuilder
                .withPayload(returnModel)
                .setHeader(KafkaHeaders.TOPIC, newTopic.name())
                .build();
        returnModelKafkaTemplate.send(message);
    }


}
