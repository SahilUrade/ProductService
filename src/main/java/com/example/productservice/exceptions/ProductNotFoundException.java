package com.example.productservice.exceptions;

public class ProductNotFoundException extends Exception {
    public ProductNotFoundException() {

    }

    public ProductNotFoundException(String message) {
        super(message);
    }
}
