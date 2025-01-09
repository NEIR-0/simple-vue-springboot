package com.restApi.RestAPI.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.restApi.RestAPI.dto.outputDTO.ResponseDTOOutput;
import com.restApi.RestAPI.model.auth.Users;
import com.restApi.RestAPI.model.token.Tokens;
import com.restApi.RestAPI.repository.TokensRepository;
import com.restApi.RestAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TokensService {
    @Autowired
    private RabbitMQSenderService rabbitMQSenderService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokensRepository tokensRepository;

    public List<Tokens> getAllTokens(){
        return tokensRepository.findAllTokensOrderByCreatedAtDesc();
    }

    public ResponseDTOOutput createTransactions(Tokens inputUser, Long userId) {
        ResponseDTOOutput responseStatus = new ResponseDTOOutput();

        // Membuat transaksi baru di database
        Tokens token = new Tokens();
        token.setSymbol(inputUser.getSymbol());
        token.setName(inputUser.getName());
        token.setTokenPrice(inputUser.getTokenPrice());
        token.setHash(inputUser.getHash());
        token.setAddressCreator(inputUser.getAddressCreator());
        token.setAddressToken(inputUser.getAddressToken());
        token.setTotalSupply(inputUser.getTotalSupply());
        token.setTotalBurn(inputUser.getTotalBurn());
        token.setAlreadyBurn(inputUser.getAlreadyBurn());
        token.setApprove(inputUser.isApprove());
        token.setStatus(inputUser.getStatus());
        token.setProfitPersen(inputUser.getProfitPersen());
        token.setAddressToken(inputUser.getAddressToken());

        Optional<Users> user = userRepository.findById(userId);
        token.setCreatorId(user.orElse(null));
        tokensRepository.save(inputUser);

        try {
            // Mengirim transaksi ke RabbitMQ
            rabbitMQSenderService.sendMessageForTokens(token);
            responseStatus.setMsg("token is being processed.");
            responseStatus.setStatus("success");
        } catch (JsonProcessingException e) {
            System.out.println("Error RabbitMQ Transaction: " + e.getMessage());
            responseStatus.setMsg("Failed to process transaction.");
            responseStatus.setStatus("failed");
        }

        return responseStatus;
    }
}
