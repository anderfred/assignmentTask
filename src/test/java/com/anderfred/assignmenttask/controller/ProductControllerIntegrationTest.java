package com.anderfred.assignmenttask.controller;

import com.anderfred.assignmenttask.model.Product;
import com.anderfred.assignmenttask.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
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

import java.util.Objects;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class ProductControllerIntegrationTest {
    @Autowired
    ProductService productService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    private Product product = null;
    private Product product2 = null;
    private Product product3 = null;

    @BeforeEach
    void setUp() {
        product = new Product(100d, "testProduct", "testProductSku");
        product.setId(1L);
        product2 = new Product(20.5d, "testProduct1", "testProduct1Sku");
        product2.setId(2L);
        product3 = new Product(0.5d, "testProduct2", "testProduct2Sku");
        product3.setId(3L);
        productService.save(product);
        productService.save(product2);
        productService.save(product3);
    }

    @Test
    void shouldMatchSetupSet() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/product/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[0].name", Matchers.is("testProduct")))
                .andExpect(jsonPath("$[0].price", Matchers.is(100d)))
                .andExpect(jsonPath("$[0].sku", Matchers.is("testProductSku")))
                .andExpect(jsonPath("$[1].name", Matchers.is("testProduct1")))
                .andExpect(jsonPath("$[1].price", Matchers.is(20.5d)))
                .andExpect(jsonPath("$[1].sku", Matchers.is("testProduct1Sku")))
                .andExpect(jsonPath("$[2].name", Matchers.is("testProduct2")))
                .andExpect(jsonPath("$[2].price", Matchers.is(0.5d)))
                .andExpect(jsonPath("$[2].sku", Matchers.is("testProduct2Sku")));
    }

    @Test
    void shouldGetFirstIdElement() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/product/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        Assertions.assertNotNull(response);
        Assertions.assertNotEquals(response, "");
        Assertions.assertEquals(mapper.readValue(response, Product.class), product);
    }

    @Test
    void shouldGetThirdIdElement() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/product/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        Assertions.assertNotNull(response);
        Assertions.assertNotEquals(response, "");
        Assertions.assertEquals(mapper.readValue(response, Product.class), product3);
    }

    @Test
    void shouldGetNotFoundCode() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/product/33")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateNewEntry() throws Exception {
        Product newProduct = new Product(133d, "newProduct", "newProductSku");
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/product/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newProduct))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        Assertions.assertNotNull(response);
        Assertions.assertNotEquals(response, "");
        Product responseProduct = mapper.readValue(response, Product.class);
        Assertions.assertNotNull(responseProduct);
        newProduct.setId(responseProduct.getId());
        Assertions.assertEquals(responseProduct, newProduct);
        Assertions.assertTrue(productService.getAll().contains(responseProduct));
    }

    @Test
    void shouldUpdateFirstElement() throws Exception {
        product.setName("updatedName");
        product.setPrice(321.01d);
        product.setSku("updatedSku");
        MvcResult result = mvc.perform(MockMvcRequestBuilders.put("/product/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(product))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        Assertions.assertNotNull(response);
        Assertions.assertNotEquals(response, "");
        Product responseProduct = mapper.readValue(response, Product.class);
        Assertions.assertNotNull(responseProduct);
        Assertions.assertEquals(321.01d, responseProduct.getPrice());
        Assertions.assertEquals("updatedName", responseProduct.getName());
        Assertions.assertEquals("updatedSku", responseProduct.getSku());
        Assertions.assertTrue(productService.getAll().contains(responseProduct));
    }

    @Test
    void shouldDeleteFirstElement() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/product/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
        Assertions.assertTrue(productService.getAll().stream().filter(s-> Objects.equals(s.getId(), product.getId())).findAny().isEmpty());
    }

    @Test
    void shouldDeleteAll() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/product/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
        Assertions.assertTrue(productService.getAll().isEmpty());
    }
}