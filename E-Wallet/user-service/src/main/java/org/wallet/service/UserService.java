package org.wallet.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.wallet.common.UserCreatedPayload;
import org.wallet.dto.UserDTO;
import org.wallet.entity.User;
import org.wallet.repo.IUserRepo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Service
public class UserService {

    private static Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private IUserRepo repo;

    @Autowired
    KafkaTemplate<String,Object> kafkaTemplate;

    private String USER_CREATED_TOPIC="USER-CREATED";

    public UserDTO save(UserDTO userDTO) {
        User user=mapToEntity(userDTO);
        user=repo.save(user);
        UserCreatedPayload payload=new UserCreatedPayload();
        payload.setEmail(user.getEmail());
        payload.setUserId(user.getId());
        payload.setUsername(user.getName());
        Future<SendResult<String, Object>> send = kafkaTemplate.send(USER_CREATED_TOPIC, user.getEmail(), payload);
        LOGGER.info("Pushed payload in kafka {}",send);
        return userDTO;
    }

    private User mapToEntity(UserDTO userDTO) {
        User user=new User();
        user.setEmail(userDTO.getEmail());
        user.setName(userDTO.getName());
        user.setPhone(userDTO.getPhone());
        user.setKycNumber(userDTO.getKycNumber());

        return user;
    }

    private UserDTO mapToEntity(User user) {
        UserDTO userDTO=new UserDTO();
        userDTO.setEmail(user.getEmail());
        userDTO.setName(user.getName());
        userDTO.setPhone(user.getPhone());
        userDTO.setKycNumber(user.getKycNumber());

        return userDTO;
    }
}
