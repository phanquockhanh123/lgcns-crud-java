package org.example.socialmediaspring.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.example.socialmediaspring.common.PageResponse;
import org.example.socialmediaspring.dto.auth.AuthRes;
import org.example.socialmediaspring.dto.category.CategoryRequest;
import org.example.socialmediaspring.entity.Category;
import org.example.socialmediaspring.exception.BizException;
import org.example.socialmediaspring.repository.CategoryRepository;
import org.example.socialmediaspring.utils.JsonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class CategoryServiceTest {

    @Autowired
    private  CategoryService categoryService;

    @MockBean
    private CategoryRepository categoryRepository;

    // input, output
    private CategoryRequest request;
    private Category response;
    private Category fakeCate;
    private String token;
    private  Category fakeResponse;
    private int page = 0;
    private int size = 10;

    @BeforeEach
    void initData() throws Exception {
        request = CategoryRequest.builder()
                .name("Gaming 2")
                .description("This is gaming description 2")
                .build();

        response = Category.builder()
                .id(1)
                .name("Gaming")
                .description("This is gaming description")
                .build();

        fakeResponse = Category.builder()
                .id(3)
                .name("Gaming 2")
                .description("This is gaming description 2")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();

        AuthRes authRes = new AuthRes();
        authRes.setEmail("khanhphanquoc68@gmail.com");
        authRes.setPassword("123456");

    }

    @Test
    public void createCategory_validRequest_success() {
        Mockito.when(categoryRepository.existsByName(anyString())).thenReturn(false);
        Mockito.when(categoryRepository.save(ArgumentMatchers.any())).thenReturn(response);

        var res = categoryService.createCategory(request);

        Assertions.assertThat(res.getId()).isEqualTo(1);
        Assertions.assertThat(res.getName()).isEqualTo("Gaming");
        Assertions.assertThat(res.getDescription()).isEqualTo("This is gaming description");
    }

    @Test
    public void createCategory_CategoryNameExists_fail() {
        // give
        Mockito.when(categoryRepository.existsByName(anyString())).thenReturn(true);

        // when
       var exception = assertThrows(BizException.class, () -> categoryService.createCategory(request));

       Assertions.assertThat(exception.getErrorCode()).isEqualTo("invalid.input");
       Assertions.assertThat(exception.getMessage()).isEqualTo("Category name existed");
    }

    @Test
    public void updateCategory_validRequest_success() {
        Mockito.when(categoryRepository.findById(any())).thenReturn(Optional.ofNullable(response));
        Mockito.when(categoryRepository.findCategoryByName(anyString())).thenReturn(Optional.ofNullable(response));
        Mockito.when(categoryRepository.save(ArgumentMatchers.any())).thenReturn(response);

        var res = categoryService.updateCategory(response.getId(),request);

        Assertions.assertThat(res.getId()).isEqualTo(1);
        Assertions.assertThat(res.getName()).isEqualTo("Gaming 2");
        Assertions.assertThat(res.getDescription()).isEqualTo("This is gaming description 2");
    }

    @Test
    public void updateCategory_CategoryIdNotExists_fail() {

        // give
        Mockito.when(categoryRepository.findById(response.getId())).thenReturn(Optional.empty());

        // when
        var exception = assertThrows(BizException.class, () -> categoryService.updateCategory(response.getId(), request));

        Assertions.assertThat(exception.getErrorCode()).isEqualTo("invalid.input");
        Assertions.assertThat(exception.getMessage()).isEqualTo("Category not found with id " + response.getId());
    }

    @Test
    public void updateCategory_CategoryNameExists_fail() {

        request.setName("Gaming 2");
        // give
        Mockito.when(categoryRepository.findById(response.getId())).thenReturn(Optional.ofNullable(response));
        Mockito.when(categoryRepository.findCategoryByName(request.getName())).thenReturn(Optional.ofNullable(fakeResponse));

        // when
        var exception = assertThrows(BizException.class, () -> categoryService.updateCategory(response.getId(), request));

        Assertions.assertThat(exception.getErrorCode()).isEqualTo("invalid.input");
        Assertions.assertThat(exception.getMessage()).isEqualTo("Category with title "  + request.getName() + " already exists");
    }

    @Test
    public void deleteCategory_validRequest_success() {
        Mockito.when(categoryRepository.existsById(any())).thenReturn(true);

        categoryRepository.deleteById(response.getId());

        // Then: Verify that deleteById was called with the correct ID
        verify(categoryRepository, times(1)).deleteById(response.getId());
    }

    @Test
    public void deleteCategory_InValidRequest_fail() {
        Mockito.when(categoryRepository.existsById(any())).thenReturn(false);

        var exception = assertThrows(EntityNotFoundException.class, () -> categoryService.deleteCategory(response.getId()));

        // Then: Verify that deleteById was called with the correct ID
        Assertions.assertThat(exception.getMessage()).isEqualTo("User not existed");
    }

    @Test
    public void getAllCategory_validRequest_success() {
        Pageable pageable = PageRequest.of(page, size, Sort.by("created").descending());

        Category category1 = Category.builder()
                .id(100)
                .name("Gaming")
                .build();

        Category category2 = Category.builder()
                .id(101)
                .name("Music")
                .build();

        Category category3 = Category.builder()
                .id(102)
                .name("Streamer")
                .build();

        List<Category> categoryList = Arrays.asList(category1, category2, category3);
        Page<Category> pageResponse = new PageImpl<>(categoryList, pageable, categoryList.size());

        System.out.println("Output: " + JsonUtils.objToString(pageResponse));

        // Mock the repository response
        Mockito.when(categoryRepository.findAll(pageable)).thenReturn(pageResponse);

        var res = categoryService.getAllCategories(page, size);

        System.out.println("Output: " + JsonUtils.objToString(res));

        assertEquals(page, res.getNumber());
        assertEquals(size, res.getSize());
        assertEquals(3, res.getTotalElements());
        assertEquals(1, res.getTotalPages());
        assertTrue(res.isFirst());
        assertTrue(res.isLast());
    }

    @Test
    public void getCategory_validRequest_success() {
        Mockito.when(categoryRepository.existsById(any())).thenReturn(true);
        // Mock the repository response
        Mockito.when(categoryRepository.findBookById(response.getId())).thenReturn(response);


        var res = categoryService.getCategoryById(response.getId());

        Assertions.assertThat(res.getId()).isEqualTo(1);
        Assertions.assertThat(res.getName()).isEqualTo("Gaming");
        Assertions.assertThat(res.getDescription()).isEqualTo("This is gaming description");
    }

    @Test
    public void getCategoryDetail_InValidRequest_fail() {
        Mockito.when(categoryRepository.existsById(any())).thenReturn(false);

        var exception = assertThrows(BizException.class, () -> categoryService.getCategoryById(response.getId()));

        // Then: Verify that deleteById was called with the correct ID
        Assertions.assertThat(exception.getMessage()).isEqualTo("Category not existed");
    }

}
