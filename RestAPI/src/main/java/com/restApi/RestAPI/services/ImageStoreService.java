package com.restApi.RestAPI.services;

import com.restApi.RestAPI.model.product.ImageStore;
import com.restApi.RestAPI.repository.ImageStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageStoreService {
    @Autowired
    ImageStoreRepository imageStoreRepository;

    public List<ImageStore> getAllImages(){
        return imageStoreRepository.findAll();
    }
}
