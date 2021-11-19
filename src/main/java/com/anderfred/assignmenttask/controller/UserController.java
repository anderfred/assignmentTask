package com.anderfred.assignmenttask.controller;

import com.anderfred.assignmenttask.model.User;
import com.anderfred.assignmenttask.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/all")
    public List<User> allUsers() {
        return userService.getAll();
    }
}
