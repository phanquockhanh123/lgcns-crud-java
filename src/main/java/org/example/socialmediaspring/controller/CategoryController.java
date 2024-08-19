package org.example.socialmediaspring.controller;

import jakarta.validation.Valid;
import org.example.socialmediaspring.common.ResponseFactory;
import org.example.socialmediaspring.dto.category.CategoryRequest;
import org.example.socialmediaspring.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/admin/categories")
@PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'USER')")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @Autowired
    ResponseFactory responseFactory;


    @Value("${coach.name}")
    private String coachName;


    @Value("${member.name}")
    private String memberName;

    @PostMapping
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        return responseFactory.success(categoryService.createCategory(categoryRequest));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity updateCategory(
            @PathVariable Integer id,
            @Valid @RequestBody CategoryRequest categoryRequest) {
        return responseFactory.success(categoryService.updateCategory(id, categoryRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return responseFactory.success(null);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('admin:read', 'manager:read', 'user:read')")
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

    @GetMapping("/health")
    @PreAuthorize("hasAnyAuthority('admin:read', 'manager:read', 'user:read')")
    public String findAllCategories() {
        return coachName + " study many knowledge " + memberName;
    }
}
