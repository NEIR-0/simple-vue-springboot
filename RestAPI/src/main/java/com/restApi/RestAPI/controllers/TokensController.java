package com.restApi.RestAPI.controllers;

import com.restApi.RestAPI.dto.tokenDTO.TokenDTO;
import com.restApi.RestAPI.dto.outputDTO.ResponseDTOOutput;
import com.restApi.RestAPI.dto.tokenDTO.UpdateTokenDTO;
import com.restApi.RestAPI.model.token.Tokens;
import com.restApi.RestAPI.services.TokensService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/token")
public class TokensController {
    @Autowired
    private TokensService tokensService;

    @GetMapping
    public List<TokenDTO> getAllTokens(){
        List<Tokens> tokensList =  tokensService.getAllTokens();
        return tokensList.stream().map(token ->
                new TokenDTO(
                        token.getId(),
                        token.getName(),
                        token.getTokenPrice(),
                        token.getSymbol(),
                        token.getProfitPersen(),
                        token.getStatus(),
                        token.getTotalSupply(),
                        token.getAddressToken(),
                        token.getTotalBurn(),
                        token.getAlreadyBurn()
                )
        ).collect(Collectors.toList());
    }

    @GetMapping("/{tokenId}")
    public TokenDTO getAllTokens(
            @PathVariable Long tokenId
    ){
        Optional<Tokens> token = tokensService.findById(tokenId);

        // Return response dalam bentuk TokenDTO
        return new TokenDTO(
                token.get().getId(),
                token.get().getName(),
                token.get().getTokenPrice(),
                token.get().getSymbol(),
                token.get().getProfitPersen(),
                token.get().getStatus(),
                token.get().getTotalSupply(),
                token.get().getAddressToken(),
                token.get().getTotalBurn(),
                token.get().getAlreadyBurn()
        );
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDTOOutput> createTokens(
            @RequestBody Tokens inputUser,
            HttpServletRequest request
    ) {
        ResponseDTOOutput responseStatus = new ResponseDTOOutput();
        Long userId = (Long) request.getAttribute("userId");
        ResponseDTOOutput response =  tokensService.createTokens(inputUser, userId);
        if ("success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        }else {
            responseStatus.setMsg("failed due create transactions");
            responseStatus.setStatus("failed");
            return ResponseEntity.badRequest().body(responseStatus);
        }
    }

    @PutMapping("/update/{tokenId}")
    public ResponseEntity<ResponseDTOOutput> UpdateTokens(
            @RequestBody UpdateTokenDTO updateTokenDTO,
            @PathVariable Long tokenId
    ) {
        ResponseDTOOutput responseStatus = new ResponseDTOOutput();
        ResponseDTOOutput response =  tokensService.updateTokens(updateTokenDTO, tokenId);
        if ("success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        }else {
            responseStatus.setMsg("failed due create token");
            responseStatus.setStatus("failed");
            return ResponseEntity.badRequest().body(responseStatus);
        }
    }

    @PutMapping("/update-burn/{tokenId}")
    public ResponseEntity<ResponseDTOOutput> UpdateTokens(
            @PathVariable Long tokenId
    ) {
        ResponseDTOOutput responseStatus = new ResponseDTOOutput();
        ResponseDTOOutput response =  tokensService.updateTokensBurn(tokenId);
        if ("success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        }else {
            responseStatus.setMsg("failed due create token");
            responseStatus.setStatus("failed");
            return ResponseEntity.badRequest().body(responseStatus);
        }
    }
}
