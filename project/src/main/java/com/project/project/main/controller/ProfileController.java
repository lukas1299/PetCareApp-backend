package com.project.project.main.controller;

import com.project.project.main.model.Friend;
import com.project.project.main.model.FriendRequest;
import com.project.project.main.model.Profile;
import com.project.project.main.model.User;
import com.project.project.main.repository.FriendRepository;
import com.project.project.main.repository.ProfileRepository;
import com.project.project.main.repository.UserRepository;
import com.project.project.main.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
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
    public ResponseEntity<Profile> createProfile(@PathVariable UUID id) {

        //TODO check authority user
        var user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User does not exist"));
        var profile = fromDto(user);
        user.setProfile(profile);
        userRepository.save(user);

        return ResponseEntity.ok(profileRepository.save(profile));
    }

    @PostMapping("/{id}/user/friends/add")
    public ResponseEntity<Profile> addUserToFriends(@PathVariable UUID id, @RequestBody FriendRequest friendRequest) {
        var profile = profileService.addUserToFriends(id, friendRequest);
        return ResponseEntity.ok(profile);
    }

    @DeleteMapping("/{id}/user/friends/delete")
    public void deleteFriend(@PathVariable UUID id, @RequestBody FriendRequest friendRequest){

        var user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User does not exist"));
        var friend = userRepository.findByUsernameOrEmail(friendRequest.name(), null).orElseThrow(() -> new EntityNotFoundException("User does not exist"));
        var profile = user.getProfile();

        var finalFriend = friendRepository.findByProfileIdAndUserId(profile.getId(), friend.getId());

        friendRepository.delete(finalFriend);
    }

    private Profile fromDto(User user) {
        return Profile.builder()
                .id(UUID.randomUUID())
                .user(user)
                .build();
    }
}
