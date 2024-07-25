package org.wallet.controller;



import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wallet.dto.TransactionDTO;
import org.wallet.dto.TransactionStatusDTO;
import org.wallet.entity.Transaction;
import org.wallet.service.TransactionService;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping("/doTransaction")
    ResponseEntity<String> doTransaction(@RequestBody @Valid TransactionDTO transactionDTO){
        String txnId= transactionService.doTransaction(transactionDTO);
        return new ResponseEntity<>(txnId, HttpStatus.ACCEPTED);
    }

    @GetMapping("/status/{txnId}")
    public ResponseEntity<TransactionStatusDTO> getTransactionStatus(@PathVariable String txnId){

        Transaction transaction=transactionService.getStatus(txnId);

        if(transaction==null){
            return ResponseEntity.ok(TransactionStatusDTO.builder()
                    .message("Invalid Transaction Id")
                    .build());

        }else{
            return ResponseEntity.ok(TransactionStatusDTO.builder()
                    .status(transaction.getStatus())
                    .message(transaction.getReason())
                    .build());
        }

    }

}
