package com.project.project.main.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.project.forum.model.Topic;
import com.project.project.main.model.AnimalRequest;
import com.project.project.main.model.User;

import com.project.project.main.model.UserRequest;
import com.project.project.main.model.UserUpdateRequest;
import com.project.project.main.repository.UserRepository;
import com.project.project.main.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @GetMapping("/me")
    public ResponseEntity<String> getInfoAboutMe(Authentication authentication) {
        return ResponseEntity.ok(authentication.getName());
    }

    @GetMapping("/me/all")
    public ResponseEntity<User> getUser(Authentication authentication){
        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        return ResponseEntity.ok(user);
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/non-friends")
    public ResponseEntity<List<User>> getNonFriendsUser(Authentication authentication) {
        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        return ResponseEntity.ok(userService.getNonFriendsUsers(user));
    }

    @GetMapping("/topics/all")
    public ResponseEntity<List<Topic>> getUserTopic(Authentication authentication) {

        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        return ResponseEntity.ok(user.getTopics());
    }

    @GetMapping("/image")
    public ResponseEntity<byte[]> addUserImage(Authentication authentication) {

        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        return new ResponseEntity<>(user.getPhoto(), HttpStatus.OK);
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> addUserImageById(@PathVariable("id") UUID id) {

        var user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        return new ResponseEntity<>(user.getPhoto(), HttpStatus.OK);
    }

    @GetMapping("/{name}/find")
    public ResponseEntity<List<User>> findPeople(@PathVariable("name") String name) {
        var list = userRepository.findAll().stream().filter(user -> user.getUsername().contains(name) || user.getEmail().contains(name)).toList();
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<User> updateUser(@PathVariable("id") UUID id, @RequestParam("file") MultipartFile file, @RequestParam("json") String json) throws IOException {
        var user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        var requestUser = objectMapper.readValue(json, UserUpdateRequest.class);
        user.setUsername(requestUser.username());
        user.setEmail(requestUser.email());
        user.setPhoto(file.getBytes());
        userRepository.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
