package com.example.productservice;

import com.example.productservice.models.Product;
import com.example.productservice.repositories.ProductRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class ProductServiceApplicationTests {

    @Autowired
    private ProductRepo productRepo;

    @Test
    void contextLoads() {
    }

    @Test
    void testProductRepo(){
        Long id = 1L;
        Optional<Product> p = productRepo.findById(id);
        System.out.println(p.get().getTitle());
    }

}
