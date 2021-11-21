package com.anderfred.assignmenttask.controller;

import com.anderfred.assignmenttask.model.Category;
import com.anderfred.assignmenttask.model.Product;
import com.anderfred.assignmenttask.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class CategoryControllerIntegrationTest {

    @Autowired
    CategoryService categoryService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    Category category1 = null;
    Category category2 = null;
    Category category3 = null;

    Product product1 = null;
    Product product2 = null;
    Product product3 = null;


    @BeforeEach
    void setUp() {
        product1 = new Product(100d, "testProduct", "testProductSku");
        product2 = new Product(20.5d, "testProduct1", "testProduct1Sku");
        product3 = new Product(0.5d, "testProduct2", "testProduct2Sku");

        category1 = new Category("testCategory1", Set.of(product1, product2));
        category2 = new Category("testCategory2", Set.of(product3));
        category3 = new Category("testCategory3", new HashSet<>());

        categoryService.save(category1);
        categoryService.save(category2);
        categoryService.save(category3);
    }

    @Test
    void shouldGetAllCategories() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/category/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        Assertions.assertNotNull(response);
        Assertions.assertNotEquals(response, "");
        Set<Category> categories = Set.of(mapper.readValue(response, Category[].class));
        Assertions.assertNotNull(categories);
        Assertions.assertFalse(categories.isEmpty());
        Assertions.assertTrue(categories.containsAll(Set.of(category1, category2, category3)));
    }

    @Test
    void shouldGetFirstCategory() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/category/" + category1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        Assertions.assertNotNull(response);
        Assertions.assertNotEquals(response, "");
        Category category = mapper.readValue(response, Category.class);
        Assertions.assertNotNull(category);
        Assertions.assertEquals(category, category1);
    }

    @Test
    void shouldGetThirdCategory() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/category/" + category3.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        Assertions.assertNotNull(response);
        Assertions.assertNotEquals(response, "");
        Category category = mapper.readValue(response, Category.class);
        Assertions.assertNotNull(category);
        Assertions.assertEquals(category, category3);
    }

    @Test
    void shouldCreateNewCategory() throws Exception {
        Product createdProduct = new Product(401d, "createdProduct", "32r/wer/23w/32");
        Category createdCategory = new Category("createdCategory", Set.of(createdProduct));
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/category/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createdCategory)))
                .andExpect(status().isCreated())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        Assertions.assertNotNull(response);
        Assertions.assertNotEquals(response, "");
        Category category = mapper.readValue(response, Category.class);
        Assertions.assertNotNull(category);
        Assertions.assertTrue(categoryService.findById(category.getId()).isPresent());
    }

    @Test
    void shouldUpdateCategory() throws Exception {
        category1.setName("updatedName");
        MvcResult result = mvc.perform(MockMvcRequestBuilders.put("/category/" + category1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(category1))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        Assertions.assertNotNull(response);
        Assertions.assertNotEquals(response, "");
        Category responseCategory = mapper.readValue(response, Category.class);
        Assertions.assertNotNull(responseCategory);
        Assertions.assertEquals("updatedName", responseCategory.getName());
    }

    @Test
    void shouldDeleteFirstCategory() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/category/" + category1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        Assertions.assertTrue(categoryService.findById(category1.getId()).isEmpty());
    }

    @Test
    void shouldDeleteAllCategories() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/category/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        Assertions.assertTrue(categoryService.getAll().isEmpty());
    }
}