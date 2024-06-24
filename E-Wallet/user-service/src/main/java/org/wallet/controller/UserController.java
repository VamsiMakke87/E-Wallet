package org.wallet.controller;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wallet.dto.UserDTO;
import org.wallet.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    UserService userService;

    @Autowired
    KafkaTemplate<String,Object> kafkaTemplate;

    @PostMapping("/save")
    public ResponseEntity<String> create(@RequestBody @Valid UserDTO userDTO){

        userService.save(userDTO);

        return ResponseEntity.ok("Account Created");

    }

}
