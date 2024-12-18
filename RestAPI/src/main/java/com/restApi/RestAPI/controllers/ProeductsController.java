package com.restApi.RestAPI.controllers;

import com.restApi.RestAPI.dto.outputDTO.ResponseDTOOutput;
import com.restApi.RestAPI.model.product.Products;
import com.restApi.RestAPI.services.ProductsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProeductsController {

    @Autowired
    ProductsService productsService;

    @GetMapping
    public List<Products> getAllProducts(
        @RequestParam(value = "search", required = false) String search
    ){
        return productsService.getAllProducts(search);
    }

    @GetMapping("/{productId}")
    public Products getProductsById(@PathVariable Long productId) {
        return productsService.getProductsById(productId);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ResponseDTOOutput> deleteUser(@PathVariable Long productId) {
        ResponseDTOOutput status = productsService.deleteById(productId);
        if ("success".equals(status.getStatus())) {
            return ResponseEntity.ok(status);
        }else {
            return ResponseEntity.badRequest().body(status);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDTOOutput> createProductions(@RequestBody Products inputUser) {
        ResponseDTOOutput status = productsService.createProduct(inputUser);
        if ("success".equals(status.getStatus())) {
            return ResponseEntity.ok(status);
        }else {
            return ResponseEntity.badRequest().body(status);
        }
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<ResponseDTOOutput> updateProductions(
            @RequestBody Products inputUser,
            @PathVariable Long productId) {
        ResponseDTOOutput status = productsService.updateProduct(inputUser, productId);
        if ("success".equals(status.getStatus())) {
            return ResponseEntity.ok(status);
        } else {
            return ResponseEntity.badRequest().body(status);
        }
    }

    @MessageMapping("/updateDataRealTime")
    @SendTo("/topic/updateDataRealTime")
    public List<Products> updateDataRealTime(){
        return productsService.getAllProducts("");
    }
}
