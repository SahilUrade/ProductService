package com.example.productservice.controllers;

import com.example.productservice.dtos.CreateProductRequestDTO;
import com.example.productservice.exceptions.ProductNotFoundException;
import com.example.productservice.models.Category;
import com.example.productservice.models.Product;
import com.example.productservice.services.FakeStoreProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class ProductController {
    private FakeStoreProductService service;

    public ProductController(FakeStoreProductService service) {
        this.service = service;
    }


    @GetMapping("/products/{id}")
    public Product getProductById(@PathVariable("id") Integer id) throws ProductNotFoundException {
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
    public ResponseEntity<ArrayList<Product>> getAllProducts() {
        ArrayList<Product> products = service.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping("/products")
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
    public Product updateProduct(@PathVariable("id") Integer id, @RequestBody CreateProductRequestDTO request) {
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
//    public void updateProduct(@PathVariable("id") Integer id){
//
//    }


    @DeleteMapping("/products/{id}")
    public Product deleteProductById(@PathVariable("id") Integer id) throws ProductNotFoundException {
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

}
