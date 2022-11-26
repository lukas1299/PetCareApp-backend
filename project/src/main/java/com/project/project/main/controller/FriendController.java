package com.project.project.main.controller;

import com.project.project.main.model.FriendStatus;
import com.project.project.main.model.User;
import com.project.project.main.repository.FriendRepository;
import com.project.project.main.repository.ProfileRepository;
import com.project.project.main.repository.UserRepository;
import com.project.project.main.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendController {

    private final ProfileRepository profileRepository;
    private final FriendRepository friendRepository;
    private final FriendService friendService;
    private final UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<List<User>> getUserFriends(Authentication authentication) {

        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        var profile = profileRepository.findByUserId(user.getId()).orElseThrow(() -> new EntityNotFoundException("Profile does not exists"));

        return ResponseEntity.ok(friendService.getFriend(profile));
    }

    @GetMapping("/me/waiting")
    public ResponseEntity<List<User>> getInvitations(Authentication authentication){

        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));

        var userProfile = profileRepository.findByUserId(user.getId()).get();

        return ResponseEntity.ok(friendService.getInvitations(userProfile));
    }

    @GetMapping("/{id}/accept")
    public ResponseEntity<List<User>> acceptInvitation(@PathVariable UUID id, Authentication authentication) throws Exception {

        var user = userRepository.findByUsernameOrEmail(authentication.getName(), null).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        var userProfile = profileRepository.findByUserId(user.getId()).get();

        var invitation = friendRepository.findByProfileIdAndUserId(userProfile.getId(), id).orElseThrow(() -> new Exception("Invitation does not exists"));

        if(!invitation.getFriendStatus().equals(FriendStatus.WAITING)){
            throw new Exception("Unable to accept");
        }

        invitation.setFriendStatus(FriendStatus.ACCEPTED);
        friendRepository.save(invitation);

        return ResponseEntity.ok(friendService.getFriend(invitation.getProfile()));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<List<User>> rejectFriend(@PathVariable UUID id) throws Exception {

        var invitation = friendRepository.findById(id).orElseThrow(() -> new Exception("Invitation does not exists"));

        invitation.setFriendStatus(FriendStatus.CANCELED);
        friendRepository.save(invitation);

        return ResponseEntity.ok(friendService.getFriend(invitation.getProfile()));
    }

}
