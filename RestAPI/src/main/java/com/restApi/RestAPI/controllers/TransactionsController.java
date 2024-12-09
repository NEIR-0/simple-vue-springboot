package com.restApi.RestAPI.controllers;

import com.restApi.RestAPI.dto.TransactionDTO;
import com.restApi.RestAPI.services.TransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
public class TransactionsController {
    @Autowired
    private TransactionsService transactionsService;

    @PostMapping("/create")
    public String createTransactions(@RequestBody TransactionDTO inputUser) {
        return transactionsService.createTransactions(inputUser);
    }
}
