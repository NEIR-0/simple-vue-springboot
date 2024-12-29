package com.restApi.RestAPI.controllers;

import com.restApi.RestAPI.dto.outputDTO.ResponseDTOOutput;
import com.restApi.RestAPI.model.transaction.Transactions;
import com.restApi.RestAPI.services.TransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionsController {
    @Autowired
    private TransactionsService transactionsService;

    @GetMapping
    public List<Transactions> getAllProducts(){
        return transactionsService.findAll();
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDTOOutput> createTransactions(
            @RequestBody Transactions inputUser
    ) {
        ResponseDTOOutput responseStatus = new ResponseDTOOutput();
        ResponseDTOOutput response =  transactionsService.createTransactions(inputUser);
        if ("success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        }else {
            responseStatus.setMsg("failed due create transactions");
            responseStatus.setStatus("failed");
            return ResponseEntity.badRequest().body(responseStatus);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDTOOutput> updateTransactions(@RequestBody Transactions inputUser) {
        ResponseDTOOutput status =  transactionsService.updateTransactions(inputUser);
        if ("success".equals(status.getStatus())) {
            return ResponseEntity.ok(status);
        }else {
            return ResponseEntity.badRequest().body(status);
        }
    }

    @MessageMapping("/updateTransactions")
    @SendTo("/topic/updateTransactions")
    public List<Transactions> updateTransactions() {
        return transactionsService.findAll();
    }
}
