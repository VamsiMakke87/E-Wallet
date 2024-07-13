package org.wallet.config;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.wallet.TransactionStatusEnum;
import org.wallet.common.TransactionCompletePayload;
import org.wallet.entity.Transaction;
import org.wallet.repo.ITransactionRepo;

@Configuration
public class KafkaConsumerConfig {

    private static Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerConfig.class);

    @Autowired
    ITransactionRepo transactionRepo;

    private static ObjectMapper objectMapper=new ObjectMapper();

    @KafkaListener(topics = "TXN-COMPLETED", groupId = "walletApp")
    public void consumerForTransactionCompleted(ConsumerRecord payload) throws JsonProcessingException {


        TransactionCompletePayload transactionCompletePayload = objectMapper.readValue(payload.value().toString(), TransactionCompletePayload.class);

        Transaction transaction = transactionRepo.findById(transactionCompletePayload.getId()).get();

        transaction.setStatus(transactionCompletePayload.isSuccess()? TransactionStatusEnum.SUCCESS:TransactionStatusEnum.FAILED);
        transaction.setReason(transactionCompletePayload.getReason());
        transactionRepo.save(transaction);

        LOGGER.info("Transaction Completed");


    }

}
