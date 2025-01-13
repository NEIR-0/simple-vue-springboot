package com.restApi.RestAPI.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.restApi.RestAPI.dto.outputDTO.ResponseDTOOutput;
import com.restApi.RestAPI.dto.tokenDTO.UpdateTokenDTO;
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

    public Optional<Tokens> findById(Long tokenId) {
        return tokensRepository.findById(tokenId);
    }

    public ResponseDTOOutput createTokens(Tokens inputUser, Long userId) {
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

        System.out.println("userId: " + userId + ">>>>>>>>>>>");
        Optional<Users> user = userRepository.findById(userId);
        if (user.isPresent()) {
            System.out.println("User found: " + user.get().getId());
            token.setCreatorId(user.orElse(null));
            tokensRepository.save(token);
        } else {
            System.out.println("User not found!");
        }

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

    public ResponseDTOOutput updateTokens(UpdateTokenDTO inputUser, Long tokenId) {
        ResponseDTOOutput responseStatus = new ResponseDTOOutput();

        Tokens existingToken = tokensRepository.findById(tokenId)
                .orElseThrow(() -> new RuntimeException("token not found"));

        existingToken.setProfitPersen(inputUser.getProfitPersen());
        existingToken.setStatus(inputUser.getStatus());
        existingToken.setTotalBurn(inputUser.getTotalBurn());
        existingToken.setTotalSupply(inputUser.getTotalSupply());

        tokensRepository.save(existingToken);

        responseStatus.setMsg("products updated successfully");
        responseStatus.setStatus("success");
        return responseStatus;
    }

    public ResponseDTOOutput updateTokensBurn(Long tokenId) {
        ResponseDTOOutput responseStatus = new ResponseDTOOutput();

        Tokens existingToken = tokensRepository.findById(tokenId)
                .orElseThrow(() -> new RuntimeException("token not found"));

        if(existingToken.getAlreadyBurn() != existingToken.getTotalBurn()){
            existingToken.setAlreadyBurn(existingToken.getAlreadyBurn() + 1);
        }
        tokensRepository.save(existingToken);

        responseStatus.setMsg("products updated successfully");
        responseStatus.setStatus("success");
        return responseStatus;
    }
}
