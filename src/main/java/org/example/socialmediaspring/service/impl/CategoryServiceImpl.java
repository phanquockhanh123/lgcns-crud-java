package org.example.socialmediaspring.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.socialmediaspring.common.PageResponse;
import org.example.socialmediaspring.constant.ErrorCodeConst;
import org.example.socialmediaspring.dto.category.CategoryRequest;
import org.example.socialmediaspring.entity.Book;
import org.example.socialmediaspring.entity.Category;
import org.example.socialmediaspring.exception.BizException;
import org.example.socialmediaspring.repository.BookRepository;
import org.example.socialmediaspring.repository.CategoryRepository;
import org.example.socialmediaspring.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private  final CategoryRepository categoryRepository;

    private final ModelMapper mapper;

    private  final BookRepository bookRepository;


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

        Category existsCate = categoryRepository.findById(id)
                .orElseThrow(() -> new BizException(ErrorCodeConst.INVALID_INPUT, "Category not found with id " + id));

        // check another title same exists
        categoryRepository.findCategoryByName(request.getName()).ifPresent(cat -> {
            if (!cat.getId().equals(id)) {
                throw new BizException(ErrorCodeConst.INVALID_INPUT,"Category with title " + request.getName() + " already exists");
            }
        });
        existsCate.setName(request.getName());
        existsCate.setDescription(request.getDescription());

        return existsCate;
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
