package com.project.project.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.project.main.model.User;
import com.project.project.main.model.UserRequest;
import com.project.project.main.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class JwtAuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @PostMapping("/authentication")
    public JwtResponse createAuthenticationToken(@RequestBody JwtRequest jwtRequest) {
        return authenticationService.authenticate(new JwtRequest(jwtRequest.login(), jwtRequest.password()));
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestParam("json") String json, @RequestParam("file") MultipartFile file) throws Exception {

        var userRequest = objectMapper.readValue(json, UserRequest.class);

        var user = userService.createUser(userRequest, file);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

}
