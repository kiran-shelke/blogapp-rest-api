package com.springboot.blog.controller;

import com.springboot.blog.payload.CategoryDto;
import com.springboot.blog.service.CategoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(
        name = "CRUD REST APIs for category resource"
)
public class CategoryController {
    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    //build add category rest api - http://localhost:8080/api/categories
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> addCategory(@RequestBody CategoryDto categoryDto)
    {
        CategoryDto savedCategory=categoryService.addCategory(categoryDto);
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }

    //get category rest api- http://localhost:8080/api/categories/1
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @GetMapping("{id}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable("id") Long categoryId)
    {
        CategoryDto getCategory=categoryService.getCategory(categoryId);
        return ResponseEntity.ok(getCategory);
    }

    //get all categories rest api- http://localhost:8080/api/categories
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories()
    {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    //update category rest api- http://localhost:8080/api/categories/1
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto categoryDto,
                                                      @PathVariable("id") Long categoryId)
    {
        return ResponseEntity.ok(categoryService.updateCategory(categoryDto,categoryId));
    }

    //build delete category rest api- http://localhost:8080/api/categories/1
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteCategory(@PathVariable("id") Long categoryId)
    {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok("Category deleted successfully..!");
    }
}
