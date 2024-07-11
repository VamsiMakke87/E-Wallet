package org.wallet.service;

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

    public void userCreated(TransactionCompletePayload transactionCompletePayload){

        Runnable r= ()->{
            SimpleMailMessage mailMessage=new SimpleMailMessage();

            mailMessage.setTo(transactionCompletePayload.getToUserId());
            mailMessage.setSubject("Account Created");
            mailMessage.setText("Welcome"+userCreatedPayload.getUsername()+",\n Your new account is created!");

            javaMailSender.send(mailMessage);
        };
        executor.execute(r);


    }

    public void userCreated(TransactionCompletePayload transactionCompletePayload){

        Runnable r= ()->{
            SimpleMailMessage mailMessage=new SimpleMailMessage();

            mailMessage.setTo(userCreatedPayload.getEmail());
            mailMessage.setSubject("Account Created");
            mailMessage.setText("Welcome"+userCreatedPayload.getUsername()+",\n Your new account is created!");

            javaMailSender.send(mailMessage);
        };
        executor.execute(r);


    }


}
