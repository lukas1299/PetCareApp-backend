package com.project.project.main.controller;

import com.project.project.forum.model.Topic;
import com.project.project.main.model.UserRequest;
import com.project.project.main.model.User;

import com.project.project.main.repository.UserRepository;
import com.project.project.main.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/topics/all")
    public ResponseEntity<List<Topic>> getUserTopic(){

        var user = userRepository.findById(UUID.fromString("71ce1069-d0ec-44e1-a497-84ad4b1ce603")).orElseThrow(() -> new EntityNotFoundException("User does not exists"));
        return ResponseEntity.ok(user.getTopics());
    }

    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestBody UserRequest userRequest) throws Exception {

        var user = userService.createUser(userRequest);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
