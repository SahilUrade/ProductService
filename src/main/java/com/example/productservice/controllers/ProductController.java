package com.example.productservice.controllers;

import com.example.productservice.dtos.CreateProductRequestDTO;
import com.example.productservice.exceptions.ProductNotFoundException;
import com.example.productservice.models.Product;
import com.example.productservice.services.ProductService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {
    private ProductService service;

    public ProductController(@Qualifier("selfProductService") ProductService service) {
        this.service = service;
    }


    @GetMapping("/products/{id}")
    @Cacheable(value = "product", key = "#id")
    public Product getProductById(@PathVariable("id") Long id) throws ProductNotFoundException {
        // validations
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }

        Product product = service.getProductById(id);
        if (product == null) {
            throw new ProductNotFoundException("Product not found");
        }
        return product;
    }


    @GetMapping("/products")
//    @Cacheable(value = "")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = service.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/products/category/{catTitle}")
    public List<Product> getProductsByCategory(@PathVariable("catTitle") String catTitle) throws ProductNotFoundException {
        List<Product> products = service.getProductsByCategory(catTitle);
        if (products.isEmpty()) {
            throw new ProductNotFoundException("Product not found");
        }
        return products;
    }

    @PostMapping("/products")
    @CachePut(value = "product", key = "#result.id")
    public Product createProduct(@RequestBody CreateProductRequestDTO request) {

        if (request.getDescription() == null) {
            throw new IllegalArgumentException("description cannot be null");
        }
        if (request.getTitle() == null) {
            throw new IllegalArgumentException("title cannot be null");
        }

        return service.createProduct(request.getTitle(), request.getDescription(), request.getImageURL(),
                request.getCategory().getTitle());
    }

    @PutMapping("/products/{id}")
    @CachePut(value = "product", key = "#id")
    public Product updateProduct(@PathVariable("id") Long id, @RequestBody CreateProductRequestDTO request) {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
//        if (request.getDescription() == null) {
//            throw new IllegalArgumentException("description cannot be null");
//        }
        if (request.getTitle() == null) {
            throw new IllegalArgumentException("title cannot be null");
        }

        service.updateProduct(id, request.getTitle(), request.getDescription(), request.getImageURL(),
                request.getCategory().getTitle());

        return service.getProductById(id);
    }

//    @PatchMapping("/products/{id}")
//    public void updateProduct(@PathVariable("id") Long id){
//
//    }


    @DeleteMapping("/products/{id}")
    @CacheEvict(value = "product", key = "#id")
    public Product deleteProductById(@PathVariable("id") Long id) throws ProductNotFoundException {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }

        Product product = service.getProductById(id);

        if (product == null) {
            throw new ProductNotFoundException("Product not found");
        }
        service.deleteProductById(id);

        return product;
    }

    @GetMapping("/products/{pageNo}/{pageSize}")
    public ResponseEntity<Page<Product>> getPaginatedProducts(@PathVariable("pageNo") int pageNo,
                                                              @PathVariable("pageSize") int pageSize) {
        Page<Product> products = service.getPaginatedProducts(pageNo, pageSize);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

}
