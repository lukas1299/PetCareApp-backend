package com.project.project.security;

import com.project.project.main.model.User;
import com.project.project.main.model.UserRequest;
import com.project.project.main.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class JwtAuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/authentication")
    public JwtResponse createAuthenticationToken(@RequestBody JwtRequest jwtRequest) {
        return authenticationService.authenticate(new JwtRequest(jwtRequest.login(), jwtRequest.password()));
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody UserRequest userRequest) throws Exception {

        var user = userService.createUser(userRequest);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

}
