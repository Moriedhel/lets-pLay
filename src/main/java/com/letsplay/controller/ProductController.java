package com.letsplay.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.letsplay.dto.ProductRequest;
import com.letsplay.dto.ProductResponse;
import com.letsplay.exception.ResourceNotFoundException;
import com.letsplay.model.Product;
import com.letsplay.repository.ProductRepository;

import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PermitAll
    @GetMapping
    public List<ProductResponse> list() {
        return productRepository.findAll().stream()
                .map(ProductResponse::fromProduct)
                .toList();
    }

    @PermitAll
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
        return ResponseEntity.ok(ProductResponse.fromProduct(product));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
        Product product = new Product(request.name(), request.description(), request.price(), request.quantity());
        Product saved = productRepository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductResponse.fromProduct(saved));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable String id, @Valid @RequestBody ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setQuantity(request.quantity());
        Product saved = productRepository.save(product);
        return ResponseEntity.ok(ProductResponse.fromProduct(saved));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found: " + id);
        }
        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
