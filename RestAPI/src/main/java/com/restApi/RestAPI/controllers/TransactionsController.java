package com.restApi.RestAPI.controllers;

import com.restApi.RestAPI.dto.inputUserDTO.TransactionDTOUserInput;
import com.restApi.RestAPI.dto.outputDTO.ResponseDTOOutput;
import com.restApi.RestAPI.services.TransactionsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
public class TransactionsController {
    @Autowired
    private TransactionsService transactionsService;

    @PostMapping("/create")
    public ResponseEntity<ResponseDTOOutput> createTransactions(@RequestBody TransactionDTOUserInput inputUser, HttpServletRequest request) {
        ResponseDTOOutput status =  transactionsService.createTransactions(inputUser, request);
        if ("success".equals(status.getStatus())) {
            return ResponseEntity.ok(status);
        }else {
            return ResponseEntity.badRequest().body(status);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDTOOutput> updateTransactions(@RequestBody TransactionDTOUserInput inputUser, HttpServletRequest request) {
        ResponseDTOOutput status =  transactionsService.updateTransactions(inputUser, request);
        if ("success".equals(status.getStatus())) {
            return ResponseEntity.ok(status);
        }else {
            return ResponseEntity.badRequest().body(status);
        }
    }
}
