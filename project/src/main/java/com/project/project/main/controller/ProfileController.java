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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final FriendRepository friendRepository;
    private final ProfileService profileService;

    @GetMapping("/{id}/friends")
    private ResponseEntity<List<Friend>> getUserFriends(@PathVariable UUID id) {
        //TODO check authority user
        var friendsList = friendRepository.findByProfile_id(id);

        return ResponseEntity.ok(friendsList);
    }

    @PostMapping("/{id}/user/add")
    public ResponseEntity<Profile> createProfile(@PathVariable UUID id) throws Exception {

        //TODO check authority user
        var profile = profileService.createProfile(id);

        return ResponseEntity.ok(profile);
    }

    @PostMapping("/{id}/user/friends/add")
    public void addUserToFriends(@PathVariable UUID id, @RequestBody FriendRequest friendRequest) throws Exception {
        profileService.addUserToFriends(id, friendRequest);
    }

//    @DeleteMapping("/{id}/user/friends/delete")
//    public void deleteFriend(@PathVariable UUID id, @RequestBody FriendRequest friendRequest) {
//
//        var user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User does not exist"));
//        var friend = userRepository.findByUsernameOrEmail(friendRequest.name(), null).orElseThrow(() -> new ObjectNotFoundException("User does not exist"));
//        var profile = user.getProfile();
//
//        var finalFriend = friendRepository.findByProfileIdAndUserId(profile.getId(), friend.getId());
//
//        friendRepository.delete(finalFriend.get());
//    }

    @PostMapping("/me")
    public ResponseEntity<Profile> getMyProfile() {
        var user = userRepository.findById(UUID.fromString("5a8cb23f-fa0d-4dff-8c16-0d14cd1f8113")).orElseThrow(() -> new ObjectNotFoundException("User does not exists"));
        var profile = profileRepository.findByUserId(user.getId()).orElseThrow(() -> new ObjectNotFoundException("Profile does not exist"));

        return ResponseEntity.ok(profile);
    }
}
