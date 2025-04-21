package com.example.productservice.controllers;

import com.example.productservice.dtos.CategoryRequestDTO;
import com.example.productservice.models.Category;
import com.example.productservice.services.CategoryService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController {
    private CategoryService categoryService;

    public CategoryController(@Qualifier("selfCategoryService") CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return categories;
    }

    @GetMapping("/categories/{id}")
    public Category getCategoryById(@PathVariable("id") Long id) {
        Category category = categoryService.getCategoryById(id);
        return category;
    }

    @PostMapping("/categories")
    public Category createCategory(@RequestBody CategoryRequestDTO categoryRequestDTO) {
        if(categoryRequestDTO.getTitle() == null || categoryRequestDTO.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        return categoryService.createCategory(categoryRequestDTO.getTitle());
    }

    @DeleteMapping("/categories/{id}")
    public Category deleteCategory(@PathVariable("id") Long id) throws Exception {
        Category category = categoryService.deleteCategory(id);
        return category;
    }

    @PutMapping("/categories/{id}")
    public Category updateCategory(@PathVariable("id") Long id, @RequestBody CategoryRequestDTO categoryRequestDTO) {
        Category category = categoryService.updateCategory(id, categoryRequestDTO.getTitle());
        return category;
    }

}
