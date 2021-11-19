package com.anderfred.assignmenttask.controller;

import com.anderfred.assignmenttask.model.User;
import com.anderfred.assignmenttask.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired

    ObjectMapper mapper;

    @MockBean
    UserService userService;

    User RECORD_1 = new User(1L, "fred", "xx", "dasd", "dasdsa", "czx");
    User RECORD_2 = new User(2L, "fsd", "xx", "qwaefd", "dasdsa", "czx");
    User RECORD_3 = new User(3L, "gfd", "xx", "dasd", "fsad", "qwrfas");

    @Test
    void allUsers() throws Exception {
        List<User> records = new ArrayList<>(Arrays.asList(RECORD_1, RECORD_2, RECORD_3));

        Mockito.when(userService.getAll()).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[2].userName", is("gfd")));
    }
}