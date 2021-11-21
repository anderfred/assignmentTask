package com.anderfred.assignmenttask.model;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderAmountUnitTest {


    @Test
    void setTotalAmount() {
        Product product1 = Mockito.mock(Product.class);
        Mockito.when(product1.getPrice()).thenReturn(100d);
        Product product2 = Mockito.mock(Product.class);
        Mockito.when(product2.getPrice()).thenReturn(20.5d);

        OrderItem orderItem1 = Mockito.mock(OrderItem.class);
        Mockito.when(orderItem1.getProduct()).thenReturn(product1);
        Mockito.when(orderItem1.getQuantity()).thenReturn(5L);

        OrderItem orderItem2 = Mockito.mock(OrderItem.class);
        Mockito.when(orderItem2.getProduct()).thenReturn(product2);
        Mockito.when(orderItem2.getQuantity()).thenReturn(10L);

        Order order = new Order(Set.of(orderItem1, orderItem2), 541d);

        assertEquals(541d, order.getTotalAmount());
    }
}