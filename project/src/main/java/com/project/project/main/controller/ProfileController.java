package com.project.project.main.controller;

import com.project.project.main.exception.ObjectNotFoundException;
import com.project.project.main.model.Friend;
import com.project.project.main.model.FriendRequest;
import com.project.project.main.model.Profile;
import com.project.project.main.repository.FriendRepository;
import com.project.project.main.repository.ProfileRepository;
import com.project.project.main.repository.UserRepository;
import com.project.project.main.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final FriendRepository friendRepository;
    private final ProfileService profileService;

    @GetMapping("/friends")
    private ResponseEntity<List<Friend>> getUserFriends(Authentication authentication) {
        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        var profile = profileRepository.findByUserId(user.getId()).orElseThrow(() -> new ObjectNotFoundException("Profile does not exist"));
        var friendsList = friendRepository.findByProfile_id(profile.getId());

        return ResponseEntity.ok(friendsList);
    }

    @PostMapping("/user/friends/add")
    public void addUserToFriends(Authentication authentication, @RequestBody FriendRequest friendRequest) throws Exception {
        profileService.addUserToFriends(authentication, friendRequest);
    }

    @GetMapping("/me")
    public ResponseEntity<Profile> getMyProfile(Authentication authentication) {
        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        var profile = profileRepository.findByUserId(user.getId()).orElseThrow(() -> new ObjectNotFoundException("Profile does not exist"));

        return ResponseEntity.ok(profile);
    }
}
