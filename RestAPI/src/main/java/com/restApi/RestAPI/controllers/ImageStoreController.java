package com.restApi.RestAPI.controllers;

import com.restApi.RestAPI.model.product.ImageStore;
import com.restApi.RestAPI.model.product.Products;
import com.restApi.RestAPI.services.ImageStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/image-store")
public class ImageStoreController {

    @Autowired
    ImageStoreService imageStoreService;

    @GetMapping
    public List<ImageStore> getAllProducts() {
        return imageStoreService.getAllImages();
    }

}