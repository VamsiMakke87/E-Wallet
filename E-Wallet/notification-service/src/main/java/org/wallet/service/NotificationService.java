package org.wallet.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.wallet.common.TransactionCompletePayload;
import org.wallet.common.UserCreatedPayload;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Service
public class NotificationService {

    @Autowired
    JavaMailSender javaMailSender;

    private Executor executor= Executors.newFixedThreadPool(5);

    private static Logger LOGGER= LoggerFactory.getLogger(NotificationService.class);

    public void userCreated(UserCreatedPayload userCreatedPayload){

        Runnable r= ()->{
            SimpleMailMessage mailMessage=new SimpleMailMessage();

            mailMessage.setTo(userCreatedPayload.getEmail());
            mailMessage.setSubject("Account Created");
            mailMessage.setText("Welcome"+userCreatedPayload.getUsername()+",\n Your new account is created!");

            javaMailSender.send(mailMessage);
        };
        executor.execute(r);


    }

    public void transactionSuccess(TransactionCompletePayload transactionCompletePayload){

        Runnable r= ()->{
            SimpleMailMessage mailMessage=new SimpleMailMessage();

            mailMessage.setTo(transactionCompletePayload.getFromUserEmail());
            mailMessage.setSubject("Transaction Successful");
            mailMessage.setText("Hello, \n Your account is debited with amount Rs."+ transactionCompletePayload.getAmount()+"with transaction id"+ transactionCompletePayload.getTxnId()+" is successful. Your updated balance: "+transactionCompletePayload.getFromUserBalance());

            javaMailSender.send(mailMessage);
        };

        Runnable r1= ()->{
            SimpleMailMessage mailMessage=new SimpleMailMessage();

            mailMessage.setTo(transactionCompletePayload.getToUserEmail());
            mailMessage.setSubject("Transaction Successful");
            mailMessage.setText("Hello, \n Your account is credited with amount Rs."+ transactionCompletePayload.getAmount()+"with transaction id"+ transactionCompletePayload.getTxnId()+" is successful. Your updated balance: Rs."+transactionCompletePayload.getToUserBalance());

            javaMailSender.send(mailMessage);
        };
        executor.execute(r);
        executor.execute(r1);

    }



    public void transactionFailed(TransactionCompletePayload transactionCompletePayload){
        LOGGER.info("Transcation Info:"+transactionCompletePayload);
        Runnable r= ()->{
            SimpleMailMessage mailMessage=new SimpleMailMessage();

            mailMessage.setTo(transactionCompletePayload.getFromUserEmail());
            mailMessage.setSubject("Transaction Failed");
            mailMessage.setText("Transaction with transaction id:"+transactionCompletePayload.getTxnId() + " failed due to "+transactionCompletePayload.getReason());

            javaMailSender.send(mailMessage);
        };
        executor.execute(r);
    }


}
