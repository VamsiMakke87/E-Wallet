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

    private static String transactionSuccessTopic="TXN-SUCCESS";
    private static String transactionFailedTopic="TXN-FAILED";

    private static String transactionCompleteTopic="TXN-COMPLETED";

    private static Logger LOGGER = LoggerFactory.getLogger(WalletService.class);

    @Transactional
    public void walletTransaction(TransactionPayload transactionPayload) throws ExecutionException, InterruptedException {

        Wallet fromWallet= walletRepo.findByUserId(transactionPayload.getFromUserId());
        TransactionCompletePayload transactionCompletePayload=TransactionCompletePayload.builder()
                .id(transactionPayload.getId())
                .txnId(transactionPayload.getTxnId())
                .requestId(transactionPayload.getRequestId())
                .fromUserId(transactionPayload.getFromUserId())
                .fromUserEmail(fromWallet.getEmail())
                .toUserId(transactionPayload.getToUserId())
                .amount(transactionPayload.getAmount())
                .build();



        if(fromWallet.getBalance()<transactionPayload.getAmount()){

            transactionCompletePayload.setSuccess(false);
            transactionCompletePayload.setReason("Insufficient Balance");
//            transactionCompletePayload.getFromUserEmail(fr);
            Future<SendResult<String, Object>> send = kafkaTemplate.send(transactionFailedTopic, transactionCompletePayload.getId().toString(), transactionCompletePayload);
            LOGGER.info("Pushed to Transaction Failed topic {}",send.get());

        }else{
            Wallet toWallet= walletRepo.findByUserId(transactionPayload.getToUserId());
            fromWallet.setBalance(fromWallet.getBalance()- transactionPayload.getAmount());
            toWallet.setBalance(toWallet.getBalance()+transactionPayload.getAmount());
            transactionCompletePayload.setSuccess(true);
            transactionCompletePayload.setToUserEmail(toWallet.getEmail());
            transactionCompletePayload.setFromUserBalance(fromWallet.getBalance());
            transactionCompletePayload.setToUserBalance(toWallet.getBalance());
            transactionCompletePayload.setReason("Transaction Successful");
            Future<SendResult<String, Object>> send = kafkaTemplate.send(transactionSuccessTopic, transactionCompletePayload.getId().toString(), transactionCompletePayload);
            LOGGER.info("Pushed to Transaction Success topic {}",send.get());

        }

        Future<SendResult<String, Object>> send = kafkaTemplate.send(transactionCompleteTopic, transactionCompletePayload.getId().toString(), transactionCompletePayload);
        LOGGER.info("Pushed to Transaction Completed topic {}",send.get());


    }

}
