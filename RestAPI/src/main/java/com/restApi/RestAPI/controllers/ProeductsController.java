package com.restApi.RestAPI.controllers;

import com.restApi.RestAPI.model.product.Products;
import com.restApi.RestAPI.repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProeductsController {

    @Autowired
    ProductsRepository productsRepository;

    @GetMapping
    public List<Products> getAllProducts(){
        return productsRepository.findAll();
    }

    @PostMapping("/create")
    public String createProductions(@RequestBody Products inputUser) {
        productsRepository.save(inputUser);

        return "berhasil";
//        return proeductsService.createTransactions(inputUser);
    }
}
