package com.thanhvv.user.controller;

import com.thanhvv.user.data.User;
import com.thanhvv.user.model.UserDto;
import com.thanhvv.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return this.userService.getAllUsers();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return this.userService.createUser(user);
    }

    @PostMapping("/login")
    public UserDto login(@RequestBody String username, @RequestBody String password) {
        return this.userService.login(username, password);
    }
}
