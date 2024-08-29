package org.example.socialmediaspring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.example.socialmediaspring.dto.auth.AuthRes;
import org.example.socialmediaspring.dto.category.CategoryRequest;
import org.example.socialmediaspring.entity.Category;
import org.example.socialmediaspring.repository.TokenRepository;
import org.example.socialmediaspring.service.CategoryService;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private  TokenRepository tokenRepository;

    // input, output
    private CategoryRequest request;
    private Category response;
    private String token;

    @BeforeEach
    void initData() throws Exception {
        request = CategoryRequest.builder()
                .name("Gaming")
                .description("This is gaming description")
                .build();

        response = Category.builder()
                .id(2)
                .name("Gaming")
                .description("This is gaming description")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();

        AuthRes authRes = new AuthRes();
        authRes.setEmail("khanhphanquoc68@gmail.com");
        authRes.setPassword("123456");

        // Simulate login and retrieve Bearer token
        String contentAsString = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(authRes))) // Send the AuthRes object
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JSONObject json = new JSONObject(contentAsString);
        token = json.getString("token").trim();
    }


    @Test
    void createCategory_ValidRequest_success() throws Exception {
        // given
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(request);

        // mock method categoryService
        Mockito.when(categoryService.createCategory(ArgumentMatchers.any())).thenReturn(response);

        // when
        mockMvc.perform(post("/api/v1/admin/categories")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("success").value(true));

        // then
    }

    @Test
    void createCategory_InValidRequest_BadRequest() throws Exception {
        // given
        request.setName("gam");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(request);

        // mock method categoryService
        Mockito.when(categoryService.createCategory(ArgumentMatchers.any())).thenReturn(response);

        // when - then
        mockMvc.perform(post("/api/v1/admin/categories")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("invalid.input"))
                .andExpect(jsonPath("data.name").value("Category name invalid with min length 4 character"));
    }

    @Test
    void updateCategory_ValidRequest_success() throws Exception {
        request.setName("");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(request);

        // mock method categoryService
        Mockito.when(categoryService.createCategory(ArgumentMatchers.any())).thenReturn(response);

        // when
        mockMvc.perform(put("/api/v1/admin/categories/" + response.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("success").value(true));

        // then
    }

    @Test
    void updateCategory_NullCateName_fail() throws Exception {
        // given
        request.setName("");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(request);

        // mock method categoryService
        Mockito.when(categoryService.createCategory(ArgumentMatchers.any())).thenReturn(response);

        // when
        mockMvc.perform(put("/api/v1/admin/categories/" + response.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("data.name").value("Category name is required"));

    }

    @Test
    void updateCategory_InvalidRequest_fail() throws Exception {
        // given
        request.setName("as");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(request);

        // mock method categoryService
        Mockito.when(categoryService.createCategory(ArgumentMatchers.any())).thenReturn(response);

        // when
        mockMvc.perform(put("/api/v1/admin/categories/" + response.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("data.name").value("Category name invalid with min length 4 character"));

    }

}
