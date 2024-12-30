package com.restApi.RestAPI.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.restApi.RestAPI.dto.outputDTO.ResponseDTOOutput;
import com.restApi.RestAPI.model.transaction.Transactions;
import com.restApi.RestAPI.repository.TransactionsRepository;
import com.restApi.RestAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class TransactionsService {
    @Autowired
    private TransactionsRepository transactionsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RabbitMQSenderService rabbitMQSenderService;

    public List<Transactions> findAll() {
        return transactionsRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public ResponseDTOOutput createTransactions(Transactions inputUser) {
        ResponseDTOOutput responseStatus = new ResponseDTOOutput();

        // Membuat transaksi baru di database
        Transactions transaction = new Transactions();
        transaction.setDescription(inputUser.getDescription());
        transaction.setPrice(inputUser.getPrice());
        transaction.setAddress_signed(inputUser.getAddress_signed());
        transaction.setStatus(inputUser.getStatus());

        try {
            // Mengirim transaksi ke RabbitMQ
            Long transactionId = rabbitMQSenderService.sendMessageForTransactions(transaction);
            responseStatus.setTransactionId(transactionId);
            responseStatus.setMsg("Transaction is being processed.");
            responseStatus.setStatus("success");
        } catch (JsonProcessingException e) {
            System.out.println("Error RabbitMQ Transaction: " + e.getMessage());
            responseStatus.setMsg("Failed to process transaction.");
            responseStatus.setStatus("failed");
        }

        return responseStatus;
    }

    public ResponseDTOOutput updateTransactions(Transactions inputUser) {
        ResponseDTOOutput responseStatus = new ResponseDTOOutput();
        Long TransactionId = inputUser.getId();
        System.out.println("TransactionId: " + TransactionId);

        if (TransactionId == null || inputUser.getHash() == null || inputUser.getStatus() == null) {
            responseStatus.setStatus("failed");
            responseStatus.setMsg("Invalid input data");
            return responseStatus;
        }

        Transactions transaction = transactionsRepository.findById(TransactionId)
                .orElseThrow(() -> new RuntimeException("Transactions not found"));

        transaction.setHash(inputUser.getHash());
        transaction.setStatus(inputUser.getStatus());

        String CodeSub = generateCode();
        transaction.setCode_Subscription(CodeSub);
        transactionsRepository.save(transaction);

        responseStatus.setStatus("success");
        responseStatus.setMsg(CodeSub);
        return responseStatus;
    }

    private static String generateCode() {
        int groupLength = 4;
        int totalGroups = 3;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < totalGroups; i++) {
            for (int j = 0; j < groupLength; j++) {
                char randomChar = characters.charAt(random.nextInt(characters.length()));
                code.append(randomChar);
            }
            if (i < totalGroups - 1) {
                code.append("-");
            }
        }
        return code.toString();
    }
}
