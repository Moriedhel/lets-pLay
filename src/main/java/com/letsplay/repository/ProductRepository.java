package com.letsplay.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.letsplay.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {
}
