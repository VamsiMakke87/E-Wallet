package org.wallet.config;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.wallet.common.UserCreatedPayload;
import org.wallet.entity.Wallet;
import org.wallet.repo.IWalletRepo;

@Configuration
public class KafkaConsumerConfig {

    private static Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerConfig.class);

    @Autowired
    IWalletRepo repo;

    private static ObjectMapper objectMapper=new ObjectMapper();
    @KafkaListener(topics = "USER-CREATED",groupId = "walletApp")
    public void consume(ConsumerRecord payload) throws JsonProcessingException {

        UserCreatedPayload userCreatedPayload=objectMapper.readValue(payload.value().toString(),UserCreatedPayload.class);
        LOGGER.info("Payload from kafka {}",userCreatedPayload);
        Wallet wallet=Wallet.builder()
                .userId(userCreatedPayload.getUserId())
                .balance(100.0)
                .build();

        repo.save(wallet);

    }

}
