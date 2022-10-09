package com.project.project.main.controller;

import com.project.project.forum.model.Topic;
import com.project.project.main.model.UserRequest;
import com.project.project.main.model.User;
import com.project.project.main.repository.UserRepository;
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

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/topics/all")
    public ResponseEntity<List<Topic>> getUserTopic(){

        var topics = userRepository.findById(UUID.fromString("8355fa25-d7c4-4963-9704-6eab27553e8d")).orElseThrow(() -> new EntityNotFoundException("User does not exists"));
        return ResponseEntity.ok(topics.getTopics());
    }

    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestBody UserRequest userRequest){
        var user = User.fromDto(userRequest);
        return new ResponseEntity<>(userRepository.save(user), HttpStatus.CREATED);
    }

}
