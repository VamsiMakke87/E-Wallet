package org.wallet.config;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.wallet.common.TransactionCompletePayload;
import org.wallet.common.UserCreatedPayload;
import org.wallet.service.NotificationService;

@Configuration
public class KafkaConsumerConfig {

    private static ObjectMapper objectMapper=new ObjectMapper();

    @Autowired
    NotificationService notificationService;

    private static Logger LOGGER= LoggerFactory.getLogger(KafkaConsumerConfig.class);
    @KafkaListener(topics = "USER-CREATED",groupId = "notificationApp")
    public void consumerForUserCreated(ConsumerRecord payload) throws JsonProcessingException {

        UserCreatedPayload userCreatedPayload=objectMapper.readValue(payload.value().toString(),UserCreatedPayload.class);
        notificationService.userCreated(userCreatedPayload);
        LOGGER.info("Email Will be Sent!!");

    }

    @KafkaListener(topics = "TXN-SUCCESS",groupId = "notificationApp")
    public void consumerForTransactionSuccess(ConsumerRecord payload) throws JsonProcessingException {

        TransactionCompletePayload transactionCompletePayload=objectMapper.readValue(payload.value().toString(), TransactionCompletePayload.class);
        notificationService.transactionSuccess(transactionCompletePayload);
        LOGGER.info("Email Will be Sent!!");

    }

    @KafkaListener(topics = "TXN-FAILED",groupId = "notificationApp")
    public void consumerForUserTransactionFailed(ConsumerRecord payload) throws JsonProcessingException {

        TransactionCompletePayload transactionCompletePayload=objectMapper.readValue(payload.value().toString(), TransactionCompletePayload.class);
        notificationService.transactionFailed(transactionCompletePayload);
        LOGGER.info("Email Will be Sent!!");

    }



}
