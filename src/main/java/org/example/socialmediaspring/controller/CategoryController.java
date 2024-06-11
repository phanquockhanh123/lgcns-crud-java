package org.example.socialmediaspring.controller;

import org.example.socialmediaspring.common.ResponseFactory;
import org.example.socialmediaspring.dto.category.CategoryRequest;
import org.example.socialmediaspring.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/categories")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @Autowired
    ResponseFactory responseFactory;

    @PostMapping
    public ResponseEntity createCategory(@RequestBody CategoryRequest categoryRequest) {
        return responseFactory.success(categoryService.createCategory(categoryRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity updateCategory(
            @PathVariable Integer id,
            @RequestBody CategoryRequest categoryRequest) {
        return responseFactory.success(categoryService.updateCategory(id, categoryRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return responseFactory.success(null);
    }

    @GetMapping
    public ResponseEntity findAllCategories(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "20", required = false) int size
    ) {
        return responseFactory.success(categoryService.getAllCategories(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity getCategory(@PathVariable Integer id) {
        return responseFactory.success(categoryService.getCategoryById(id));
    }

}
