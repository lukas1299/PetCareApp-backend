package com.project.project.main.service;

import com.project.project.main.model.User;
import com.project.project.main.model.UserRequest;
import com.project.project.main.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ProfileService profileService;

    public User createUser(UserRequest userRequest) throws Exception {

        var user = User.fromDto(userRequest);

        if(userRepository.findByUsernameOrEmail(userRequest.username(), userRequest.email()).isPresent()){
            throw new Exception("User currently exist");
        }

        var newUser = userRepository.save(user);
        profileService.createProfile(newUser.getId());

        return newUser;
    }
}
