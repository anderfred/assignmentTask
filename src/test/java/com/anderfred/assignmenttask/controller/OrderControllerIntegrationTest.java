package com.anderfred.assignmenttask.controller;

import com.anderfred.assignmenttask.model.Order;
import com.anderfred.assignmenttask.model.OrderItem;
import com.anderfred.assignmenttask.model.Product;
import com.anderfred.assignmenttask.service.OrderItemService;
import com.anderfred.assignmenttask.service.OrderService;
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

import java.time.Instant;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class OrderControllerIntegrationTest {
    @Autowired
    OrderService orderService;

    @Autowired
    OrderItemService orderItemService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    private Order order1 = null;
    private Order order2 = null;

    private OrderItem orderItem1 = null;
    private OrderItem orderItem2 = null;
    private OrderItem orderItem3 = null;

    private Product product1 = null;
    private Product product2 = null;
    private Product product3 = null;


    @BeforeEach
    void setUp() {
        product1 = new Product(100d, "testProduct", "testProductSku");
        product2 = new Product(20.5d, "testProduct1", "testProduct1Sku");
        product3 = new Product(0.5d, "testProduct2", "testProduct2Sku");

        orderItem1 = new OrderItem(15L, product1);
        orderItem2 = new OrderItem(3L, product2);
        orderItem3 = new OrderItem(33L, product3);

        order1 = new Order(Set.of(orderItem1), 1500.0d);
        order1.setCreatedAt(Instant.now());
        order2 = new Order(Set.of(orderItem2, orderItem3), 555d);
        order2.setCreatedAt(Instant.now());

        orderService.save(order1);
        orderService.save(order2);
    }

    @Test
    void shouldGetAllOrders() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/order/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].totalAmount", Matchers.is(1500.0d)))
                .andExpect(jsonPath("$[0].orderItems[0].quantity", Matchers.is(15)))
                .andExpect(jsonPath("$[0].orderItems[0].product.name", Matchers.is("testProduct")))
                .andExpect(jsonPath("$[1].totalAmount", Matchers.is(555d)));
    }

    @Test
    void shouldGetFirstOrder() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/order/" + order1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalAmount", Matchers.is(1500.0d)))
                .andExpect(jsonPath("$.orderItems[0].quantity", Matchers.is(15)))
                .andExpect(jsonPath("$.orderItems[0].product.name", Matchers.is("testProduct")));
    }

    @Test
    void shouldGetNotFoundCode() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/order/33")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateOrder() throws Exception {
        order1.setOrderItems(order2.getOrderItems());
        MvcResult result = mvc.perform(MockMvcRequestBuilders.put("/order/" + order1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(order1))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        Assertions.assertNotNull(response);
        Assertions.assertNotEquals(response, "");
    }

    @Test
    void shouldDeleteOrderAndOrderItems() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/order/" + order1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        Assertions.assertTrue(orderService.findById(order1.getId()).isEmpty());
        Assertions.assertTrue(orderItemService.findById(orderItem1.getId()).isEmpty());
    }

    @Test
    void deleteAllOrdersAndAllOrderItems() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/order/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        Assertions.assertTrue(orderService.getAll().isEmpty());
        Assertions.assertTrue(orderItemService.getAll().isEmpty());
    }
}