package com.example.testcases.controller;

import com.example.testcases.entity.User;
import com.example.testcases.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vulns/sqli/users")
public class SQLiController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public User getUserById(@RequestParam String id) {
        return userRepository.findById(id);
    }
}
