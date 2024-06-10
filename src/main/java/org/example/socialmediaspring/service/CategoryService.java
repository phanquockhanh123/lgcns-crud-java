package org.example.socialmediaspring.service;

import org.example.socialmediaspring.common.PageResponse;
import org.example.socialmediaspring.dto.ApiResponse;
import org.example.socialmediaspring.dto.CategoryRequest;
import org.example.socialmediaspring.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Category createCategory(CategoryRequest category);

    Category updateCategory(Integer id, CategoryRequest category);

    void deleteCategory(Integer categoryId);

    PageResponse<Category> getAllCategories(int page, int size);

    Category getCategoryById(Integer id);
}
