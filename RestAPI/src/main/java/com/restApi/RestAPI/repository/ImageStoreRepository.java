package com.restApi.RestAPI.repository;

import com.restApi.RestAPI.model.product.ImageStore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageStoreRepository extends JpaRepository<ImageStore, Long> {
}
