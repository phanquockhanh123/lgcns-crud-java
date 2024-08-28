package org.example.socialmediaspring.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.example.socialmediaspring.common.GeneralResponse;
import org.example.socialmediaspring.common.ResponseFactory;
import org.example.socialmediaspring.dto.auth.AuthRes;
import org.example.socialmediaspring.dto.category.CategoryRequest;
import org.example.socialmediaspring.dto.common.ReqRes;
import org.example.socialmediaspring.entity.Category;
import org.example.socialmediaspring.service.CategoryService;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

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
                .id(1)
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
        System.out.println("First unit test java spring boot. create category");

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
}
