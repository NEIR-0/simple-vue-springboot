package com.restApi.RestAPI.controllers;

import com.restApi.RestAPI.dto.TransactionDTO;
import com.restApi.RestAPI.model.auth.Users;
import com.restApi.RestAPI.model.transaction.Transactions;
import com.restApi.RestAPI.repository.TransactionsRepository;
import com.restApi.RestAPI.repository.UserRepository;
import com.restApi.RestAPI.services.TransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
public class TransactionsController {
    @Autowired
    private TransactionsService transactionsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    TransactionsRepository transactionsRepository;

    @PostMapping("/create")
    public String createTransactions(@RequestBody TransactionDTO inputUser) {
        System.out.println(inputUser.getUserId() + "??????"); // 1
        System.out.println(inputUser.getPrice() + "??????");

        Users user = userRepository.findById(inputUser.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Users receiver = null;
        if (inputUser.getReceiverId() != null) {
            receiver = userRepository.findById(inputUser.getReceiverId())
                    .orElseThrow(() -> new RuntimeException("Receiver not found"));
        }

        Transactions transaction = new Transactions();
        transaction.setUser(user);
        transaction.setDes(inputUser.getDes());
        transaction.setPrice(inputUser.getPrice());
        transaction.setHash(inputUser.getHash());
        transaction.setStatus(inputUser.getStatus());

        // Simpan ke database
        transactionsRepository.save(transaction);

        return "Transaction created successfully!";
    }
}
