package org.example.socialmediaspring.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.socialmediaspring.common.PageResponse;
import org.example.socialmediaspring.dto.BookResponse;
import org.example.socialmediaspring.dto.CategoryRequest;
import org.example.socialmediaspring.dto.UserResponse;
import org.example.socialmediaspring.entity.BookEntity;
import org.example.socialmediaspring.entity.Category;
import org.example.socialmediaspring.entity.UserEntity;
import org.example.socialmediaspring.exception.ExceptionResponse;
import org.example.socialmediaspring.repository.CategoryRepository;
import org.example.socialmediaspring.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private  final CategoryRepository categoryRepository;

    @Override
    public Category createCategory(CategoryRequest category) {
        Category newCat = new Category();
        newCat.setName(category.getName());
        newCat.setDescription(category.getDescription());

        return categoryRepository.save(newCat);
    }

    @Override
    public Category updateCategory(Integer id, CategoryRequest request) {

        return categoryRepository.findById(id)
                .map(category1 -> {
                    category1.setName(request.getName());
                    category1.setDescription(request.getDescription());

                    Category newCat = categoryRepository.save(category1);
                    return newCat;
                })
                .orElseThrow(() -> new EntityNotFoundException("Category existed"));
    }

    @Override
    public void deleteCategory(Integer categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new EntityNotFoundException("User not existed");
        }

        categoryRepository.deleteById(categoryId);
    }

    @Override
    public PageResponse<Category> getAllCategories(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("created").descending());

        Page<Category> categories = categoryRepository.findAll(pageable);

        List<Category> categoryList = categories.stream()
               .toList();
        return new PageResponse<>(
                categoryList,
                categories.getNumber(),
                categories.getSize(),
                categories.getTotalElements(),
                categories.getTotalPages(),
                categories.isFirst(),
                categories.isLast()
        );
    }

    @Override
    public Optional<Category> getCategoryById(Integer id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("User not existed");
        }

        Optional<Category> category = categoryRepository.findById(id);
        return category;
    }
}
