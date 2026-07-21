package com.letsplay.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.letsplay.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {

    List<Product> findByOwnerId(String ownerId);
}
