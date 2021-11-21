package com.anderfred.assignmenttask.controller;

import com.anderfred.assignmenttask.repository.OrderRepository;
import com.anderfred.assignmenttask.service.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
class ReportControllerUnitTest {
    @Autowired
    OrderService orderService;

    @MockBean
    private OrderRepository orderRepository;

    @Test
    void shouldMatchTwoOrders(){
        Object[] firstOrder = new Object[2];
        Object[] secondOrder = new Object[2];

        firstOrder[0] = new Date();
        firstOrder[1] = 321d;

        secondOrder[0] = Date.from(Instant.now().minus(5, ChronoUnit.DAYS));
        secondOrder[1] = 444.5231d;
        List<Object[]> data = new ArrayList<>(List.of(firstOrder, secondOrder));
        when(orderRepository.findReportData()).thenReturn(data);
        Assertions.assertEquals("[\"2021-11-21:321,00\",\"2021-11-16:444,52\"]", (orderService.findReportData()));
    }

    @Test
    void shouldReturnEmptyArray() {
        List<Object[]> data = new ArrayList<>();
        when(orderRepository.findReportData()).thenReturn(data);
        Assertions.assertEquals("[]", (orderService.findReportData()));
    }

}