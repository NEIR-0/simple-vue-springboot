package com.restApi.RestAPI.services;

import com.restApi.RestAPI.config.JwtUtil;
import com.restApi.RestAPI.dto.inputUserDTO.TransactionDTOUserInput;
import com.restApi.RestAPI.dto.outputDTO.ResponseDTOOutput;
import com.restApi.RestAPI.model.auth.Users;
import com.restApi.RestAPI.model.transaction.Transactions;
import com.restApi.RestAPI.repository.TransactionsRepository;
import com.restApi.RestAPI.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionsService {
    @Autowired
    private TransactionsRepository transactionsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public List<Transactions> findAll() {
        return transactionsRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public ResponseDTOOutput createTransactions(TransactionDTOUserInput inputUser, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        ResponseDTOOutput responseStatus = new ResponseDTOOutput();

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            Long id = jwtUtil.getIdFromToken(token);

            Users user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

            Transactions transaction = new Transactions();
            transaction.setUser(user);
            transaction.setDes(inputUser.getDes());
            transaction.setPrice(inputUser.getPrice());
            transaction.setHash(inputUser.getHash());
            transaction.setStatus(inputUser.getStatus());
            Transactions newTransactions = transactionsRepository.save(transaction);

            responseStatus.setTransactionId(newTransactions.getId());
            responseStatus.setMsg("transactions created successfully");
            responseStatus.setStatus("success");
            return responseStatus;
        }else{
            responseStatus.setMsg("failed due create transactions");
            responseStatus.setStatus("failed");
            return responseStatus;
        }
    }

    public ResponseDTOOutput updateTransactions(TransactionDTOUserInput inputUser, HttpServletRequest request) {
        ResponseDTOOutput responseStatus = new ResponseDTOOutput();
        Long TransactionId = inputUser.getTransactionId();

        if (TransactionId == null || inputUser.getHash() == null || inputUser.getStatus() == null) {
            responseStatus.setStatus("failed");
            responseStatus.setMsg("Invalid input data");
            return responseStatus;
        }

        Transactions transaction = transactionsRepository.findById(TransactionId)
                .orElseThrow(() -> new RuntimeException("Transactions not found"));

        transaction.setHash(inputUser.getHash());
        transaction.setStatus(inputUser.getStatus());
        transactionsRepository.save(transaction);

        responseStatus.setStatus("success");
        responseStatus.setMsg("transactions updated successfully");
        return responseStatus;
    }
}
