package com.project.project.controller;

import com.project.project.model.User;
import com.project.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    @PostMapping("/save")
    public void save(){
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName("Ukis");
        userRepository.save(user);

    }
}
