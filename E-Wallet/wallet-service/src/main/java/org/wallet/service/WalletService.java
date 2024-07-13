package org.wallet.service;


import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.wallet.common.NotificationStatusEnum;
import org.wallet.common.TransactionCompletePayload;
import org.wallet.common.TransactionPayload;
import org.wallet.entity.Wallet;
import org.wallet.repo.IWalletRepo;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class WalletService {

    @Autowired
    IWalletRepo walletRepo;


    @Autowired
    KafkaTemplate<String,Object> kafkaTemplate;

    private static String transactionCompleteTopic="TXN-COMPLETED";

    private static Logger LOGGER = LoggerFactory.getLogger(WalletService.class);

    @Transactional
    public void walletTransaction(TransactionPayload transactionPayload) throws ExecutionException, InterruptedException {

        Wallet fromWallet= walletRepo.findByUserId(transactionPayload.getFromUserId());
        TransactionCompletePayload transactionCompletePayload=TransactionCompletePayload.builder()
                .id(transactionPayload.getId())
                .requestId(transactionPayload.getRequestId())
                .fromUserId(transactionPayload.getFromUserId())
                .toUserId(transactionPayload.getToUserId())
                .amount(transactionPayload.getAmount())
                .build();



        if(fromWallet.getBalance()<transactionPayload.getAmount()){

            transactionCompletePayload.setSuccess(false);
            transactionCompletePayload.setReason("Insufficient Balance");
            transactionCompletePayload.setNotificationStatus(NotificationStatusEnum.FAILED);

        }else{
            Wallet toWallet= walletRepo.findByUserId(transactionPayload.getToUserId());
            fromWallet.setBalance(fromWallet.getBalance()- transactionPayload.getAmount());
            toWallet.setBalance(toWallet.getBalance()+transactionPayload.getAmount());
            transactionCompletePayload.setSuccess(true);
            transactionCompletePayload.setReason("Transaction Successful");
            transactionCompletePayload.setNotificationStatus(NotificationStatusEnum.SUCCESS);
        }
        Future<SendResult<String, Object>> send = kafkaTemplate.send(transactionCompleteTopic, transactionCompletePayload.getId().toString(), transactionCompletePayload);
        LOGGER.info("Pushed to Transaction Completed topic {}",send.get());

    }

}
