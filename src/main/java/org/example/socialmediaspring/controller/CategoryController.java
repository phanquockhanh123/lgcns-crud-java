package org.example.socialmediaspring.controller;

import org.example.socialmediaspring.common.ResponseFactory;
import org.example.socialmediaspring.dto.category.CategoryRequest;
import org.example.socialmediaspring.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/categories")
@PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @Autowired
    ResponseFactory responseFactory;

    @PostMapping
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity createCategory(@RequestBody CategoryRequest categoryRequest) {
        return responseFactory.success(categoryService.createCategory(categoryRequest));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity updateCategory(
            @PathVariable Integer id,
            @RequestBody CategoryRequest categoryRequest) {
        return responseFactory.success(categoryService.updateCategory(id, categoryRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return responseFactory.success(null);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('admin:read', 'manager:read')")
    public ResponseEntity findAllCategories(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "20", required = false) int size
    ) {
        return responseFactory.success(categoryService.getAllCategories(page, size));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'manager:read')")
    public ResponseEntity getCategory(@PathVariable Integer id) {
        return responseFactory.success(categoryService.getCategoryById(id));
    }

}
