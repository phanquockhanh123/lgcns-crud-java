package org.example.socialmediaspring.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.socialmediaspring.common.PageResponse;
import org.example.socialmediaspring.constant.ErrorCodeConst;
import org.example.socialmediaspring.dto.CategoryRequest;
import org.example.socialmediaspring.entity.Category;
import org.example.socialmediaspring.exception.BizException;
import org.example.socialmediaspring.repository.CategoryRepository;
import org.example.socialmediaspring.service.CategoryService;
import org.modelmapper.ModelMapper;
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

    private final ModelMapper mapper;


    @Override
    public Category createCategory(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new BizException(ErrorCodeConst.INVALID_INPUT, "Category name existed");
        }

        Category newCat = new Category();
        newCat.setName(request.getName());
        newCat.setDescription(request.getDescription());

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
    public Category getCategoryById(Integer id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("User not existed");
        }
        Category category = categoryRepository.findBookById(id);

        return category;
    }
}
