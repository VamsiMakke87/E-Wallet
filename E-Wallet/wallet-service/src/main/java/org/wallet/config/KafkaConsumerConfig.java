package org.wallet.config;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.wallet.common.TransactionPayload;
import org.wallet.common.UserCreatedPayload;
import org.wallet.entity.Wallet;
import org.wallet.repo.IWalletRepo;
import org.wallet.service.WalletService;

import java.util.concurrent.ExecutionException;

@Configuration
public class KafkaConsumerConfig {

    private static Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerConfig.class);

    @Autowired
    IWalletRepo walletRepo;

//    @Autowired

    @Autowired
    WalletService walletService;

    private static ObjectMapper objectMapper=new ObjectMapper();
    @KafkaListener(topics = "USER-CREATED",groupId = "walletApp")
    public void consumeFromUserPayload(ConsumerRecord payload) throws JsonProcessingException {

        UserCreatedPayload userCreatedPayload=objectMapper.readValue(payload.value().toString(),UserCreatedPayload.class);
        LOGGER.info("Payload from kafka {}",userCreatedPayload);
        MDC.put("requestId",userCreatedPayload.getRequestId());
        Wallet wallet=Wallet.builder()
                .userId(userCreatedPayload.getUserId())
                .email(userCreatedPayload.getEmail())
                .balance(100.0)
                .build();

        walletRepo.save(wallet);
        MDC.clear();

    }

    @KafkaListener(topics = "TXN-INIT",groupId = "walletApp")
    public void consumeFromTransactionPayload(ConsumerRecord payload) throws JsonProcessingException, ExecutionException, InterruptedException {
        LOGGER.info("Inside do transaction");
        TransactionPayload transactionPayload=objectMapper.readValue(payload.value().toString(), TransactionPayload.class);
        LOGGER.info("Payload from kafka {}",transactionPayload);
        MDC.put("requestId",transactionPayload.getRequestId());

        walletService.walletTransaction(transactionPayload);


        MDC.clear();

    }





}
