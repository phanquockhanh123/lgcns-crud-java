package org.example.socialmediaspring.controller;

import org.example.socialmediaspring.common.PageResponse;
import org.example.socialmediaspring.dto.ApiResponse;
import org.example.socialmediaspring.dto.BookResponse;
import org.example.socialmediaspring.dto.CategoryRequest;
import org.example.socialmediaspring.dto.UserResponse;
import org.example.socialmediaspring.entity.Category;
import org.example.socialmediaspring.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/admin/categories")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @PostMapping
    public ApiResponse<Category> createCategory(@RequestBody CategoryRequest categoryRequest) {
        return ApiResponse.<Category>builder()
                .result(categoryService.createCategory(categoryRequest))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<Category> updateCategory(
            @PathVariable Integer id,
            @RequestBody CategoryRequest categoryRequest) {
        return ApiResponse.<Category>builder()
                .result(categoryService.updateCategory(id, categoryRequest))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return ApiResponse.<String>builder()
                .result("Category has been deleted")
                .build();
    }

    @GetMapping
    public ResponseEntity<PageResponse<Category>> findAllCategories(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "20", required = false) int size
    ) {
        return ResponseEntity.ok(categoryService.getAllCategories(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Category>> getCategory(@PathVariable Integer id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

}
